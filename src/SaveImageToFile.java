import org.geotools.data.FileDataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.*;
import org.opengis.filter.FilterFactory;
import org.opengis.referencing.FactoryException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * Created by san on 02/07/14.
 */
public class SaveImageToFile {

    static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
    static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

    public static void main(String[] args) throws IOException, FactoryException, CQLException {
        long st = System.currentTimeMillis();
       // FileDataStore store = new ShapefileDataStore(new File("data/landuase.shp").toURI().toURL());

        Style style = createPolygonStyle();
        //Layer layer = new FeatureLayer(store.getFeatureSource(), style);

        MapContent map = new MapContent();
        //map.addLayer(layer);


        double[] box = getWGS84LngLat(9275, 21747, 16);
        ReferencedEnvelope bbox = new ReferencedEnvelope(box[0], box[2], box[1], box[3], CRS.decode("EPSG:4326"));


//arender
        Rectangle paintArea = new Rectangle(0, 0, 256, 256);
        BufferedImage image = new BufferedImage(paintArea.width, paintArea.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gr = image.createGraphics();
        gr.setPaint(Color.WHITE);
        gr.fill(paintArea);


        GTRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(map);
        renderer.paint(gr, paintArea, bbox);


        File fileToSave = new File("test.png");
        ImageIO.write(image, "png", fileToSave);

        long ed = System.currentTimeMillis();
        System.out.println("complete:" + (ed - st));
    }

    /**
     * Create a Style to draw polygon features with a thin blue outline and
     * a cyan fill
     */
    private static Style createPolygonStyle() {

        // create a partially opaque outline stroke
        org.geotools.styling.Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(Color.BLUE),
                filterFactory.literal(1),
                filterFactory.literal(0.5));

        // create a partial opaque fill
        Fill fill = styleFactory.createFill(
                filterFactory.literal(Color.CYAN),
                filterFactory.literal(0.5));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }
    private static double[] getWGS84LngLat(int x, int y, int zoom) {
        double padding = 0;
        double left = (x - padding) * (360.0 / (1 << (zoom + 1))) - 180;
        double right = (x + 1 + padding) * (360.0 / (1 << (zoom + 1))) - 180;
        double top = 90 - (y - padding) * (180.0 / (1 << zoom));
        double bottom = 90 - (y + 1 + padding) * (180.0 / (1 << zoom));
        return new double[]{left, bottom, right, top};
    }
}
