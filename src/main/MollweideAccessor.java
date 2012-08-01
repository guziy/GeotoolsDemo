package main;

/**
 * User: san
 * Date: 11/12/11
 * Time: 11:36 AM
 */
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class MollweideAccessor {

    public static final String wktMollweide = "PROJCS[\"World_Mollweide\"," +
            "GEOGCS[\"WGS_1984\",DATUM[\"WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]]," +
            "PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Mollweide\"]," +
            "PARAMETER[\"Central_Meridian\",0.0],UNIT[\"Meter\",1.0]]";


    public static final String wktEckert = "PROJCS[\"World_Eckert_IV\"," +
            "GEOGCS[\"WGS_1984\",DATUM[\"WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],"+
            "PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Eckert_IV\"]," +
            "PARAMETER[\"Central_Meridian\",0.0],UNIT[\"Meter\",1.0]]";

    public static CoordinateReferenceSystem getMollweideCRS() throws FactoryException{
        return CRS.parseWKT(wktMollweide);
    }

    public static CoordinateReferenceSystem getEckertCRS() throws FactoryException{
        return CRS.parseWKT(wktEckert);
    }

    public static void main(String[] args) throws FactoryException {
        CoordinateReferenceSystem theCRS = CRS.parseWKT(wktMollweide);
        System.out.println(theCRS);
    }
}