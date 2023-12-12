package com.mapledsl.nebula.session;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.session.MapleDslSessionTemplate;
import com.mapledsl.nebula.NebulaGraphBaseTest;
import com.vesoft.nebula.client.graph.NebulaPoolConfig;
import com.vesoft.nebula.client.graph.data.HostAddress;

import java.util.Collections;

public class NebulaGraphSessionBaseTest extends NebulaGraphBaseTest {
    static final MapleNebulaDslSessionFactory mapleNebulaDslSessionFactory = new MapleNebulaDslSessionFactory(MapleDslConfiguration.primaryConfiguration(),
            Collections.singletonList(new HostAddress("127.0.0.1", 9669)),
            "maple",
            "root", "nebula",
            new NebulaPoolConfig().setMaxConnSize(5).setMinConnSize(1)
    );
    static final MapleNebulaDslSessionFactory mapleHashNebulaDslSessionFactory = new MapleNebulaDslSessionFactory(MapleDslConfiguration.primaryConfiguration(),
            Collections.singletonList(new HostAddress("127.0.0.1", 9669)),
            "maple_hash",
            "root", "nebula",
            new NebulaPoolConfig().setMaxConnSize(5).setMinConnSize(1)
    );
    static final MapleDslSessionTemplate sessionTemplate = MapleDslSessionTemplate.newInstance(mapleNebulaDslSessionFactory);
    static final MapleDslSessionTemplate hashSessionTemplate = MapleDslSessionTemplate.newInstance(mapleHashNebulaDslSessionFactory);
}
