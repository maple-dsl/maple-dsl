package com.mapledsl.core.extension;

import org.apache.commons.codec.digest.MurmurHash2;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * This enum represents different strategies for generating key policies.
 */
public enum KeyPolicyStrategies implements KeyPolicyStrategy {
    /**
     * Internally this is just a placeholder class and is handled as a special case.
     * The id is assigned after creation of the node in the database.
     */
    INTERNAL {
        @Override
        public Object generate(Object source) {
            return null;
        }
    },
    /**
     * input value by developer in stern.
     */
    MANUAL {
        @Override
        public Object generate(Object source) {
            return source;
        }
    },
    /**
     * generate value by UUID
     */
    UUID {
        @Override
        public Object generate(Object source) {
            return java.util.UUID.randomUUID().toString();
        }
    },
    /**
     * generate value by MurmurHash2, using seed(0xc70f6907)
     * <a href="https://docs.nebula-graph.io/3.2.0/3.ngql-guide/6.functions-and-expressions/16.type-conversion/#hash">NebulaGraph#hash</a>
     */
    HASH {
        @Override
        public Object generate(Object source) {
            final byte[] data = source.toString().getBytes(UTF_8);
            return MurmurHash2.hash64(data, data.length, 0xc70f6907);
        }
    }
}