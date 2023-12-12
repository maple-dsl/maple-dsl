package com.mapledsl.nebula.module;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.extension.introspect.BeanDefinition;
import com.mapledsl.core.model.Model;
import com.mapledsl.core.module.MapleDslDefinitionResultHandler;
import com.mapledsl.nebula.model.NebulaModel;
import com.vesoft.nebula.Edge;
import com.vesoft.nebula.Tag;
import com.vesoft.nebula.Value;
import com.vesoft.nebula.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of the {@link MapleDslDefinitionResultHandler} interface.
 * Handles the conversion of {@link Value} objects into bean definitions.
 */
public class DefaultMapleNebulaDslDefinitionResultHandler implements MapleDslDefinitionResultHandler<Value> {
    static final Logger LOG = LoggerFactory.getLogger(DefaultMapleNebulaDslDefinitionResultHandler.class);

    @Override
    public Class<Value> inboundType() {
        return Value.class;
    }

    @Override
    public <OUT> OUT apply(Value value, BeanDefinition<OUT> definition, MapleDslConfiguration context) {
        if (value.getSetField() != Value.VVAL && value.getSetField() != Value.EVAL) {
            LOG.warn("Type:{} does not supported `unwrap` yet.", value.getSetField());
            return null;
        }

        final OUT out = definition.newInstance();
        if (value.getSetField() == Value.VVAL) {
            LOG.debug("Label:{} unwrap as vertex.", definition.label());
            final Vertex vertex = value.getVVal();
            definition.setter(out, Model.V.ID, vertex.vid);

            if (vertex.tags.isEmpty()) return out;
            final Tag tag = vertex.tags.get(0);
            if (tag == null) return out;

            if (tag.isSetName()) definition.setter(out, Model.V.TAG, new String(tag.name).intern());
            if (tag.isSetProps()) tag.props.forEach((k, v) -> definition.setter(out, new String(k).intern(), v));

            return out;
        }

        if (value.getSetField() == Value.EVAL) {
            LOG.debug("Label:{} unwrap as edge.", definition.label());
            final Edge edge = value.getEVal();

            definition.setter(out, Model.E.SRC, edge.type > 0 ? edge.src : edge.dst);
            definition.setter(out, Model.E.DST, edge.type > 0 ? edge.dst : edge.src);
            definition.setter(out, Model.E.TAG, new String(edge.name).intern());
            definition.setter(out, NebulaModel.E.RANK, edge.ranking);

            if (edge.isSetProps()) edge.props.forEach((k, v) -> definition.setter(out, new String(k).intern(), v));

            return out;
        }

        return out;
    }
}
