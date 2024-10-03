package own.eteryz;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class CognitiveModelApplication {
    public static void main(String[] args) throws Exception {
        CognitiveModel cognitiveModel = ReadFileUtils.readExcel("/models/cognitive-model.xlsx");
        Yaml yaml = new Yaml();
        InputStream inputStream = CognitiveModelApplication.class.getClassLoader().getResourceAsStream("application.yaml");
        Map<String, Object> obj = yaml.load(inputStream);
        String url = (String) ((Map<?, ?>) obj.get("database")).get("url");
        String login = (String) ((Map<?, ?>) obj.get("database")).get("login");
        String password = (String) ((Map<?, ?>) obj.get("database")).get("password");
        Neo4jGraphBuilder graphBuilder = new Neo4jGraphBuilder(url, login, password);
        graphBuilder.cleanGraph();
        graphBuilder.buildGraph(cognitiveModel);
        graphBuilder.close();
    }
}
