package main;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/**
 * User: san
 * Date: 08/12/12
 * Time: 2:26 AM
 */
public class FilterDemo {


    public static void main(String[] args) throws SchemaException {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point geometry = geometryFactory.createPoint(new Coordinate(38.9, -77));


        LineString ls = geometryFactory.createLineString(new Coordinate[]{
              new Coordinate(51.5, -0.5), new Coordinate(38.9, -77), new Coordinate(40, -78)
        });

        SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
        b.setName("Point");
        b.add("name", String.class );
        b.crs( DefaultGeographicCRS.WGS84 );
        b.add("the_geom", Point.class);

        final SimpleFeatureType TYPE = b.buildFeatureType();



        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(TYPE);

        SimpleFeature simpleFeature = builder.buildFeature("featureName");
        simpleFeature.setDefaultGeometry(geometry);
        //simpleFeature.set("the_geom", geometry);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Filter filter = ff.contains(ff.literal(ls), ff.property("the_geom"));
        System.out.println(simpleFeature.getDefaultGeometry().toString());
        System.out.println(ls.toString());
        boolean match = filter.evaluate(simpleFeature);
        System.out.println(match);

    }
}
