package com.mapledsl.neo4j.module;

import com.google.common.collect.Iterators;
import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.extension.introspect.BeanDefinition;
import com.mapledsl.core.model.Model;
import com.mapledsl.core.module.MapleDslDefinitionResultHandler;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.neo4j.driver.internal.types.InternalTypeSystem.TYPE_SYSTEM;

public class DefaultMapleNeo4jDslDefinitionResultHandler implements MapleDslDefinitionResultHandler<Value> {
    static final Logger LOG = LoggerFactory.getLogger(DefaultMapleNeo4jDslDefinitionResultHandler.class);

    @Override
    public Class<Value> inboundType() {
        return Value.class;
    }

    @Override
    public <OUT> OUT apply(Value value, BeanDefinition<OUT> definition, MapleDslConfiguration context) {
        if (value.type() != TYPE_SYSTEM.NODE() && value.type() != TYPE_SYSTEM.RELATIONSHIP()) {
            LOG.warn("Type:{} does not supported `unwrap` yet.", value.type());
            return null;
        }

        final OUT out = definition.newInstance();
        if (value.type() == TYPE_SYSTEM.NODE()) {
            LOG.debug("Label:{} unwrap as vertex.", definition.label());
            final Node vertex = value.asNode();
            if (vertex == null) return out;
            definition.setter(out, Model.V.TAG, Iterators.get(vertex.labels().iterator(), 0));

            if (vertex.size() == 0) return out;
            vertex.asMap().forEach((k,v) -> definition.setter(out, k, v));

            return out;
        }

        if (value.type() == TYPE_SYSTEM.RELATIONSHIP()) {
            LOG.debug("Label:{} unwrap as edge.", definition.label());
            final Relationship edge = value.asRelationship();
            if (edge == null) return out;
            definition.setter(out, Model.E.TAG, edge.type());

            if (edge.size() == 0) return out;
            edge.asMap().forEach((k,v) -> definition.setter(out, k, v));

            return out;
        }

        return out;
    }
}
