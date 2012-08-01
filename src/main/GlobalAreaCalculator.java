package main;

import com.vividsolutions.jts.geom.Geometry;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.io.File;
import java.io.IOException;

/**
 * User: san
 * Date: 08/12/11
 * Time: 10:41 AM
 *
 * Calculates global lake area using GLWD data set
 */
public class GlobalAreaCalculator {
    public static final String localPath = "data/WWFBinaryitem6598/glwd_2.shp";
    public static final String globalPath = "data/WWFBinaryitem6597/glwd_1.shp";

    private MathTransform coordTransform = null;
    

    public GlobalAreaCalculator(CoordinateReferenceSystem sourceCRS) throws FactoryException {
        CoordinateReferenceSystem eqAreaCRS = MollweideAccessor.getMollweideCRS();
        coordTransform = CRS.findMathTransform(sourceCRS, eqAreaCRS, true);
    }


    /**
     *
     * @param features - feature iterator
     * @return calculated area in km^2
     * @throws TransformException
     */
    private double getArea(FeatureIterator features) throws TransformException {
        SimpleFeature feature;
        Geometry geometry;
        double area = 0;
        while ( features.hasNext() ) {
           feature =  (SimpleFeature) features.next();
           geometry = (Geometry) feature.getDefaultGeometry();
           geometry = JTS.transform(geometry, coordTransform);
           area += geometry.getArea();
        }
        return area;
    }


    private double getAreaFromProperty(FeatureIterator features, String propertyName){
        SimpleFeature feature;
        double area = 0;
        while ( features.hasNext() ) {
            feature =  (SimpleFeature) features.next();
            area += Double.parseDouble(feature.getProperty(propertyName).getValue().toString());
        }
        return area;
    }
    
    
    public static void main(String[] args) throws IOException, FactoryException, TransformException {
        // get feature results
        
        String [] paths = new String[]{
                localPath, globalPath
        };

        GlobalAreaCalculator globalAreaCalculator;
        
        double area = 0;
        double propArea = 0;
        for (String thePath : paths){
            
            FileDataStore store = FileDataStoreFinder.getDataStore(new File(thePath));
            String name = store.getTypeNames()[0];

            SimpleFeatureSource source = store.getFeatureSource(name);


            CoordinateReferenceSystem sourceCRS = source.getSchema().getCoordinateReferenceSystem();
            if (sourceCRS == null){
                sourceCRS = DefaultGeographicCRS.WGS84;
            }
            
            globalAreaCalculator = new GlobalAreaCalculator(sourceCRS);
    
            FeatureCollection featureCollection = source.getFeatures();
            FeatureIterator features = featureCollection.features();
    
            area += globalAreaCalculator.getArea(features);
            propArea += globalAreaCalculator.getAreaFromProperty(featureCollection.features(), "AREA_SKM");
            features.close();
        }
        System.out.printf("Calculated lake area: %f km^2 \n", area / 1.0e6);
        System.out.printf("Area taken from properties: %f km^2 \n", area / 1.0e6);

        System.out.printf("Global area: %.3f km^2 \n", 4 * Math.PI * Math.pow(6371, 2));


    }
}
