package com.mapledsl.nebula.model;

import com.mapledsl.core.exception.MapleDslException;
import com.mapledsl.core.model.ID;

import static com.mapledsl.core.MapleDslDialectRenderHelper.quote;

public class NebulaEdgeID<R> implements ID {
    private final R src;
    private final R dst;
    private final long rank;

    public NebulaEdgeID(R src, R dst) {
        this(src, dst, 0L);
    }

    public NebulaEdgeID(R src, R dst, long rank) {
        if (src == null) throw new MapleDslException("NebulaEdge#src must not be null.");
        if (dst == null) throw new MapleDslException("NebulaEdge#dst must not be null.");
        if (rank < 0) throw new MapleDslException("NebulaEdge#rank must not be negative.");
        this.src = src;
        this.dst = dst;
        this.rank = rank;
    }

    @Override
    public String fragment() {
        return src instanceof String && dst instanceof String ?
                quote((String) src) + "->" + quote(((String) dst)) + "@" + rank :
                src + "->" + dst + "@" + rank;
    }
}
