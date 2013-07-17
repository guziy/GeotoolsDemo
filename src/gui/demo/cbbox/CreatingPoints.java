package gui.demo.cbbox;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.tool.CursorTool;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.style.ContrastMethod;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;


public class CreatingPoints {

	private static StyleFactory sf = CommonFactoryFinder.getStyleFactory();
    private static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    private static AbstractGridCoverage2DReader reader;
    
    
    SimpleFeatureSource featureSource;
    protected JMapFrame mapFrame;
    
    private SampleCombo cmbFrame;
    private Print printFrame;
    
    MapContent map = new MapContent();
    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );

    DefaultFeatureCollection pointCollection;
    Layer pointLayer;

    SimpleFeatureTypeBuilder pointFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
    SimpleFeatureType pointType = null;


    Style pointStyle = SLD.createPointStyle("Circle",Color.BLACK, Color.YELLOW, 1, 8);

/*
    public CreatingPoint(){



    }

*/

    public static void main(String[] args) throws Exception {
        CreatingPoints me = new CreatingPoints();


        //To define location of Shape file and Image
        File file = new File("data/MCR Boundary/MCR_BOUNDARY.shp");

    /*
        File img = new File("ikonos_RJM_URBAN.tif");
    	AbstractGridFormat format = GridFormatFinder.findFormat( img );        
    	reader = format.getReader(img);
     */
        me.displayShapefile(file);

    }
    public void displayShapefile(File file) throws Exception {
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        featureSource = store.getFeatureSource();

        //MapContent map = new MapContent();
        map.setTitle("Municipal Corporation of Rajahmundry");
        
        //Create style to Shape file
        Style style = SLD.createPolygonStyle(Color.BLUE, Color.CYAN, 0f, null, null);
        Layer layer = new FeatureLayer(featureSource, style);
        
        //Create style to Raster Image
/*
        Style imgstyle = createRGBStyle();
        Layer imglayer = new GridReaderLayer(reader, imgstyle);

        map.addLayer(imglayer);
        map.addLayer(layer);
*/

        mapFrame = new JMapFrame(map);

        mapFrame.enableToolBar(true);
        mapFrame.enableStatusBar(true);
        
        JToolBar toolBar = mapFrame.getToolBar();
        
        //Button for creating Points
        
        ImageIcon Create = new ImageIcon("data/Create.PNG");
        JButton btn = new JButton(Create);
        
        //JButton btn = new JButton("Create");
        toolBar.addSeparator();
        toolBar.add(btn);

        //create a layer for points
        createPointLayer();
        this.cmbFrame = null;
		this.printFrame = null;
		//mapFrame.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR ) );   
       
		btn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mapFrame.getMapPane().setCursorTool(
                        new CursorTool(){
                            @Override
                            public void onMouseClicked(MapMouseEvent ev) {
                                createFeatures(ev);

                            }
                        });
            }
        });
        
        
        //Button for Exit
        ImageIcon Exit = new ImageIcon("data/Exit.PNG");
        JButton bt = new JButton(Exit);
        toolBar.addSeparator();
        toolBar.add(bt);
        class BtListener implements ActionListener {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.exit(1);
			}
        	
        }
        bt.addActionListener (new BtListener ());

        mapFrame.setSize(700, 700);
        mapFrame.setVisible(true);
    }


    /**
*
* @param ev - has a method getWorldPos()
*/
    void createFeatures(MapMouseEvent ev) {
        double[] xy = ev.getWorldPos().getCoordinate();
        Point point = geometryFactory.createPoint( new Coordinate( xy[0], xy[1] ));
        pointCollection.add(SimpleFeatureBuilder.build(pointType, new Object[]{point}, null));

        map.removeLayer(pointLayer);
        pointLayer = new FeatureLayer(pointCollection, pointStyle);
        map.addLayer(pointLayer);
        
        
        if(cmbFrame != null || printFrame != null)
        	
		{
			//getWorkbench().getFrame().remove(cmbFrame);
			cmbFrame.doDefaultCloseAction();
			printFrame.doDefaultCloseAction();
		}
			
			cmbFrame = new SampleCombo(point);
			//getWorkbench().getFrame().addInternalFrame(cmbFrame );
			
			cmbFrame.setLocation(750, 100);
			mapFrame.getLayeredPane().add(cmbFrame);
			
			//mapFrame.add(cmbFrame);
			//mapFrame.getMapPane().add(cmbFrame);
			
			
			printFrame = new Print();
			//getWorkbench().getFrame().addInternalFrame(printFrame);
			printFrame.setLocation(750, 300);
			mapFrame.getLayeredPane().add(printFrame);
		
			

        System.out.println(MessageFormat.format("Created Point: {0}", point));
    }


    /**
*
*/
    private void createPointLayer(){
        if (pointType == null){
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

/*
    private Feature createFeatureForPoint(Point point){
        return null;
    }
  */  
    private static Style createRGBStyle() {
		// TODO Auto-generated method stub
		GridCoverage2D cov = null;
        try {
            cov = reader.read(null);
        } catch (IOException giveUp) {
          throw new RuntimeException(giveUp);
        }
        // We need at least three bands to create an RGB style
        int numBands = cov.getNumSampleDimensions();
        if (numBands < 3) {
            return null;
        }
        // Get the names of the bands
        String[] sampleDimensionNames = new String[numBands];
        for (int i = 0; i < numBands; i++) {
            GridSampleDimension dim = cov.getSampleDimension(i);
            sampleDimensionNames[i] = dim.getDescription().toString();
        }
        final int RED = 0, GREEN = 1, BLUE = 2;
        int[] channelNum = { -1, -1, -1 };
        // We examine the band names looking for "red...", "green...", "blue...".
        // Note that the channel numbers we record are indexed from 1, not 0.
        for (int i = 0; i < numBands; i++) {
            String name = sampleDimensionNames[i].toLowerCase();
            if (name != null) {
                if (name.matches("red.*")) {
                    channelNum[RED] = i + 1;
                } else if (name.matches("green.*")) {
                    channelNum[GREEN] = i + 1;
                } else if (name.matches("blue.*")) {
                    channelNum[BLUE] = i + 1;
                }
            }
        }
        // If we didn't find named bands "red...", "green...", "blue..."
        // we fall back to using the first three bands in order
        if (channelNum[RED] < 0 || channelNum[GREEN] < 0 || channelNum[BLUE] < 0) {
            channelNum[RED] = 1;
            channelNum[GREEN] = 2;
            channelNum[BLUE] = 3;
        }
        // Now we create a RasterSymbolizer using the selected channels
        SelectedChannelType[] sct = new SelectedChannelType[cov.getNumSampleDimensions()];
        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
        for (int i = 0; i < 3; i++) {
            sct[i] = sf.createSelectedChannelType(String.valueOf(channelNum[i]), ce);
        }
        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        ChannelSelection sel = sf.channelSelection(sct[RED], sct[GREEN], sct[BLUE]);
        sym.setChannelSelection(sel);

        return SLD.wrapSymbolizers(sym);
	}

}