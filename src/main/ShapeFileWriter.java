package main;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * User: huziy
 * Date: 31/07/12
 * Time: 10:57 PM
 */
public class ShapeFileWriter {



    public static void main(String[] args) throws IOException, SchemaException {


        final SimpleFeatureType TYPE = DataUtilities.createType("Location",
                "geometry:Polygon:srid=4326," + // <- the geometry attribute: Point type
                        "name:String," + // <- a String attribute
                        "number:Integer" // a number attribute
        );


        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("url", new File("test.shp").toURI().toURL()); // path to the output shape file
        params.put("create spatial index", Boolean.TRUE);

        ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
        newDataStore.createSchema(TYPE);

        /*
         * You can comment out this line if you are using the createFeatureType method (at end of
         * class file) rather than DataUtilities.createType
         */
        //newDataStore.forceSchemaCRS(DefaultGeographicCRS.WGS84);




        /*
         * GeometryFactory will be used to create the geometry attribute of each feature (a Point
         * object for the location)
         */
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);

        /* Create points and add to the feature collection */
        Point p;
        DefaultFeatureCollection defaultFeatureCollection = new DefaultFeatureCollection(null, TYPE);


        for (int i = 0; i < 100; i+=20) {
            p = geometryFactory.createPoint(new Coordinate(-180 + i, -90 + i));
            featureBuilder.add(p.buffer(5));
            featureBuilder.add(Integer.toString(i) + "---");
            featureBuilder.add(i);
            defaultFeatureCollection.add(featureBuilder.buildFeature(null));

        }


        /*
        * Write the features to the shapefile
        */
        Transaction transaction = new DefaultTransaction("create");

        String typeName = newDataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);



        if (featureSource instanceof SimpleFeatureStore) {

            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

            featureStore.setTransaction(transaction);
            try {
                featureStore.addFeatures(defaultFeatureCollection);
                transaction.commit();

            } catch (Exception problem) {
                problem.printStackTrace();
                transaction.rollback();

            } finally {
                transaction.close();
            }
            System.exit(0); // success!
        } else {
            System.out.println(typeName + " does not support read/write access");
            System.exit(1);
        }

    }
}
