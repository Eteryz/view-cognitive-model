package own.eteryz;

import org.neo4j.driver.*;

public class Neo4jGraphBuilder {
    private final Driver driver;

    public Neo4jGraphBuilder(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public void close() {
        driver.close();
    }

    // Метод для построения графа в Neo4j на основе модели
    public void buildGraph(CognitiveModel model) {
        try (Session session = driver.session()) {
            String[] labels = model.labels();
            // Создание узлов (вершин)
            for (String label : labels) {
                session.run("CREATE (model:Concept {name: $name})", Values.parameters("name", label));
            }

            int[][] adjacencyMatrix = model.adjacencyMatrix();
            int size = adjacencyMatrix.length;
            // Создание рёбер (связей)
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    int weight = adjacencyMatrix[i][j];
                    if (weight != 0) {
                        String query = "MATCH (a:Concept {name: $from}), (b:Concept {name: $to}) " +
                                "CREATE (a)-[:RELATION {weight: $weight}]->(b)";
                        session.run(query, Values.parameters(
                                "from", labels[i],
                                "to", labels[j],
                                "weight", weight
                        ));
                    }
                }
            }
        }
    }

    public void cleanGraph() {
        try (Session session = driver.session()) {
            session.run("MATCH (model) DETACH DELETE model;");
        }
    }
}
