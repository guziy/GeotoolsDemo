import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.tool.CursorTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.MessageFormat;


public class RoughCreatingPoint {

    private JMapFrame mapFrame;
    //private com.vividsolutions.jts.geom.Point point;
    MapContent map = new MapContent();
    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);




    public static void main(String[] args) throws Exception {
        RoughCreatingPoint me = new RoughCreatingPoint();


        //Change back to the path on your system
        File file = new File("data/MCR Boundary/MCR_BOUNDARY.shp");

        me.displayShapefile(file);

    }
    public void displayShapefile(File file) throws Exception {
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();

        //MapContent map = new MapContent();
        map.setTitle("Creating Points");
        Style style = SLD.createPolygonStyle(Color.BLUE, Color.CYAN, 0.5f, null, null);
        Layer layer = new FeatureLayer(featureSource, style);
        map.addLayer(layer);
        mapFrame = new JMapFrame(map);
        mapFrame.enableToolBar(true);
        mapFrame.enableStatusBar(true);
        JToolBar toolBar = mapFrame.getToolBar();
        JButton btn = new JButton("Create");
        toolBar.addSeparator();
        toolBar.add(btn);

        btn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mapFrame.getMapPane().setCursorTool(
                        new CursorTool() {

                            @Override
                            public void onMouseClicked(MapMouseEvent ev) {
                                createFeatures(ev);
                            }
                        });
            }
        });


        mapFrame.setSize(600, 600);
        mapFrame.setVisible(true);
    }


    /**
     *
     * @param ev - has a method getWorldPos()
     */
    void createFeatures(MapMouseEvent ev) {

        double[] xy = ev.getWorldPos().getCoordinate();
        Point point = geometryFactory.createPoint( new Coordinate( xy[0], xy[1] ));
        System.out.println(MessageFormat.format("Created Point: {0}", point));
    }



}