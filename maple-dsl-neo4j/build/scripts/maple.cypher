CREATE DATABASE maple IF NOT EXISTS

:use maple
CREATE (p1:person {id: 'p001', name: 'bofa',age: 26})
CREATE (p2:person {id: 'p002', name: 'zhangsan',age: 30})
CREATE (p3:person {id: 'p003', name: 'lisi',age: 32})
CREATE (p1)-[:impact {id:'e001', src:'p001', dst:'p002', type: 'type1'}]->(p2)
CREATE (p1)-[:impact {id:'e002', src:'p001', dst:'p002', type: 'type2'}]->(p2)

CREATE DATABASE maple_hash IF NOT EXISTS
:use maple_hash
CREATE (p1:person {id: 1, name: 'bofa',age: 26})
CREATE (p2:person {id: 2, name: 'zhangsan',age: 30})
CREATE (p3:person {id: 3, name: 'lisi',age: 32})
CREATE (p1)-[:impact {id:1, src:1, dst:2, type: 'type1'}]->(p2)
CREATE (p1)-[:impact {id:2, src:1, dst:2, type: 'type2'}]->(p2)
