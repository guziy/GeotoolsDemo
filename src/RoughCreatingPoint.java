import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.MessageFormat;

import javax.swing.*;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.FeatureListener;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.memory.MemoryFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.CollectionEvent;
import org.geotools.feature.CollectionListener;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.map.event.MapLayerListEvent;
import org.geotools.map.event.MapLayerListener;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.JMapPane;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.tool.CursorTool;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;


public class RoughCreatingPoint {


    SimpleFeatureSource featureSource;
    private JMapFrame mapFrame;
    //private com.vividsolutions.jts.geom.Point point;
    MapContent map = new MapContent();
    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

    DefaultFeatureCollection pointCollection;
    Layer pointLayer;

    SimpleFeatureTypeBuilder pointFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
    SimpleFeatureType pointType = null;
    private static float POINT_SIZE = 10;

    Style pointStyle = SLD.createPointStyle("Circle", Color.RED, Color.RED, 0.5f, POINT_SIZE);


    public RoughCreatingPoint() {


    }


    public static void main(String[] args) throws Exception {

        final RoughCreatingPoint me = new RoughCreatingPoint();
        //Change back to the path on your system
        final File file = new File("data/MCR Boundary/MCR_BOUNDARY.shp");

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    me.displayShapefile(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void displayShapefile(File file) throws Exception {
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        featureSource = store.getFeatureSource();

        //MapContent map = new MapContent();
        map.setTitle("Creating Points");
        Style style = SLD.createPolygonStyle(Color.BLUE, Color.CYAN, 0.5f, null, null);
        Layer layer = new FeatureLayer(featureSource, style);
        map.addLayer(layer);


        mapFrame = new JMapFrame(map);

        mapFrame.enableToolBar(true);
        mapFrame.enableStatusBar(true);

        //mapFrame.enableLayerTable(true);


        JToolBar toolBar = mapFrame.getToolBar();
        JButton btn = new JButton("Create");
        toolBar.addSeparator();
        toolBar.add(btn);

        //create a layer for points
        createPointLayer();

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


        mapFrame.setSize(700, 700);
        mapFrame.setVisible(true);
    }


    /**
     * @param ev - has a method getWorldPos()
     */
    void createFeatures(MapMouseEvent ev) {
        double[] xy = ev.getWorldPos().getCoordinate();
        Point point = geometryFactory.createPoint(new Coordinate(xy[0], xy[1]));
        pointCollection.add(SimpleFeatureBuilder.build(pointType, new Object[]{point}, null));

        //create map layer event
        MapLayerEvent mple = new MapLayerEvent(pointLayer, MapLayerEvent.DATA_CHANGED);
        //create maplayer list event
        MapLayerListEvent mplle = new MapLayerListEvent(map, pointLayer, map.layers().indexOf(pointLayer), mple);

        mapFrame.getMapPane().layerChanged(mplle);
        System.out.println(MessageFormat.format("Created Point: {0}", point));
    }


    /**
     *
     */
    private void createPointLayer() {
        if (pointType == null) {
            pointFeatureTypeBuilder.setName("Point");
            pointFeatureTypeBuilder.setCRS(featureSource.getSchema().getCoordinateReferenceSystem());
            pointFeatureTypeBuilder.add("geom", Point.class);
            pointType = pointFeatureTypeBuilder.buildFeatureType();
            pointCollection = new DefaultFeatureCollection(null, pointType);
        }


        pointLayer = new FeatureLayer(pointCollection, pointStyle);
        map.addLayer(pointLayer);
        mapFrame.getMapPane();
    }


    private Feature createFeatureForPoint(Point point) {
        return null;
    }

}