package pilot;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LevelController {

    DocumentBuilder builder;

    public LevelController() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            this.builder = factory.newDocumentBuilder();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public HexMapModel getMapForLevel(int level) {
        HexMapModel map = null;
        try {

            File xml = new File("data/levels/01.xml");
            Document levelData = this.builder.parse(xml);
            levelData.getDocumentElement().normalize();

            Element area = (Element)levelData.getElementsByTagName("area").item(0);
            map = new HexMapModel(Integer.parseInt(area.getAttribute("startx")),
                                  Integer.parseInt(area.getAttribute("starty")),
                                  Integer.parseInt(area.getAttribute("width")),
                                  Integer.parseInt(area.getAttribute("height")));

            int[] shipCoords = getCoords((Element)levelData.getElementsByTagName("ship").item(0));
            map.addShip(shipCoords);

            NodeList stars = levelData.getElementsByTagName("star");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public int[] getCoords(Element el) {
        Element coordEl = (Element)el.getElementsByTagName("coords").item(0);
        int[] coords = new int[]{Integer.parseInt(coordEl.getAttribute("x")),
                                 Integer.parseInt(coordEl.getAttribute("y"))};
        return coords;
    }
}
