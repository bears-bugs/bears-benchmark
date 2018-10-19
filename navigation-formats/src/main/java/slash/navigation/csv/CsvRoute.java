/*
    This file is part of RouteConverter.

    RouteConverter is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    RouteConverter is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with RouteConverter; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Copyright (C) 2007 Christian Pesch. All Rights Reserved.
*/
package slash.navigation.csv;

import slash.common.type.CompactCalendar;
import slash.navigation.base.BaseRoute;
import slash.navigation.base.SimpleFormat;
import slash.navigation.base.SimpleRoute;
import slash.navigation.base.Wgs84Position;
import slash.navigation.base.Wgs84Route;
import slash.navigation.bcr.BcrFormat;
import slash.navigation.bcr.BcrPosition;
import slash.navigation.bcr.BcrRoute;
import slash.navigation.excel.ExcelFormat;
import slash.navigation.excel.ExcelPosition;
import slash.navigation.excel.ExcelRoute;
import slash.navigation.gopal.GoPalPosition;
import slash.navigation.gopal.GoPalRoute;
import slash.navigation.gopal.GoPalRouteFormat;
import slash.navigation.gpx.GpxFormat;
import slash.navigation.gpx.GpxPosition;
import slash.navigation.gpx.GpxRoute;
import slash.navigation.itn.TomTomPosition;
import slash.navigation.itn.TomTomRoute;
import slash.navigation.itn.TomTomRouteFormat;
import slash.navigation.kml.BaseKmlFormat;
import slash.navigation.kml.KmlPosition;
import slash.navigation.kml.KmlRoute;
import slash.navigation.nmea.BaseNmeaFormat;
import slash.navigation.nmea.NmeaPosition;
import slash.navigation.nmea.NmeaRoute;
import slash.navigation.nmn.NmnFormat;
import slash.navigation.nmn.NmnPosition;
import slash.navigation.nmn.NmnRoute;
import slash.navigation.photo.PhotoFormat;
import slash.navigation.tcx.TcxFormat;
import slash.navigation.tcx.TcxRoute;

import java.util.ArrayList;
import java.util.List;

import static slash.navigation.base.RouteCharacteristics.Track;
import static slash.navigation.base.RouteComments.createRouteName;

/**
 * A CSV route.
 *
 * @author Christian Pesch
 */

public class CsvRoute extends BaseRoute<CsvPosition, CsvFormat> {
    private String name;
    private List<CsvPosition> positions;

    public CsvRoute(CsvFormat format, String name, List<CsvPosition> positions) {
        super(format, Track);
        this.name = name;
        this.positions = positions;
    }

    public String getName() {
        return name != null ? name : createRouteName(positions);
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDescription() {
        return null;
    }

    public List<CsvPosition> getPositions() {
        return positions;
    }

    public int getPositionCount() {
        return positions.size();
    }

    public void add(int index, CsvPosition position) {
        positions.add(index, position);
    }

    public CsvPosition createPosition(Double longitude, Double latitude, Double elevation, Double speed, CompactCalendar time, String description) {
        return new CsvPosition(longitude, latitude, elevation, speed, time, description);
    }

    protected BcrRoute asBcrFormat(BcrFormat format) {
        List<BcrPosition> bcrPositions = new ArrayList<>();
        for (CsvPosition position : getPositions()) {
            bcrPositions.add(position.asMTPPosition());
        }
        return new BcrRoute(format, getName(), getDescription(), bcrPositions);
    }

    protected CsvRoute asCsvFormat(CsvFormat format) {
        List<CsvPosition> positions = new ArrayList<>(getPositions());
        return new CsvRoute(format, getName(), positions);
    }

    protected ExcelRoute asExcelFormat(ExcelFormat format) {
        List<ExcelPosition> excelPositions = new ArrayList<>();
        for (CsvPosition position : getPositions()) {
            excelPositions.add(position.asMicrosoftExcelPosition());
        }
        return new ExcelRoute(format, getName(), excelPositions);
    }

    protected GoPalRoute asGoPalRouteFormat(GoPalRouteFormat format) {
        List<GoPalPosition> gopalPositions = new ArrayList<>();
        for (CsvPosition position : getPositions()) {
            gopalPositions.add(position.asGoPalRoutePosition());
        }
        return new GoPalRoute(format, getName(), gopalPositions);
    }

    protected GpxRoute asGpxFormat(GpxFormat format) {
        List<GpxPosition> gpxPositions = new ArrayList<>();
        for (CsvPosition position : getPositions()) {
            gpxPositions.add(position.asGpxPosition());
        }
        return new GpxRoute(format, getCharacteristics(), getName(), getDescription(), gpxPositions);
    }

    protected KmlRoute asKmlFormat(BaseKmlFormat format) {
        List<KmlPosition> kmlPositions = new ArrayList<>();
        for (CsvPosition position : getPositions()) {
            kmlPositions.add(position.asKmlPosition());
        }
        return new KmlRoute(format, getCharacteristics(), getName(), getDescription(), kmlPositions);
    }

    protected NmeaRoute asNmeaFormat(BaseNmeaFormat format) {
        List<NmeaPosition> nmeaPositions = new ArrayList<>();
        for (CsvPosition position : getPositions()) {
            nmeaPositions.add(position.asNmeaPosition());
        }
        return new NmeaRoute(format, getCharacteristics(), nmeaPositions);
    }

    protected NmnRoute asNmnFormat(NmnFormat format) {
        List<NmnPosition> nmnPositions = new ArrayList<>();
        for (CsvPosition position : getPositions()) {
            nmnPositions.add(position.asNmnPosition());
        }
        return new NmnRoute(format, getCharacteristics(), getName(), nmnPositions);
    }

    protected SimpleRoute asPhotoFormat(PhotoFormat format) {
        List<Wgs84Position> wgs84Positions = new ArrayList<>();
        for (CsvPosition position : getPositions()) {
            wgs84Positions.add(position.asWgs84Position());
        }
        return new Wgs84Route(format, getCharacteristics(), wgs84Positions);
    }

    protected SimpleRoute asSimpleFormat(SimpleFormat format) {
        List<Wgs84Position> positions = new ArrayList<>();
        for (CsvPosition position : getPositions()) {
            positions.add(position.asWgs84Position());
        }
        return new Wgs84Route(format, getCharacteristics(), positions);
    }

    protected TcxRoute asTcxFormat(TcxFormat format) {
        List<Wgs84Position> wgs84Positions = new ArrayList<>();
        for (CsvPosition position : getPositions()) {
            wgs84Positions.add(position.asWgs84Position());
        }
        return new TcxRoute(format, getCharacteristics(), getName(), wgs84Positions);
    }

    protected TomTomRoute asTomTomRouteFormat(TomTomRouteFormat format) {
        List<TomTomPosition> tomTomPositions = new ArrayList<>();
        for (CsvPosition position : getPositions()) {
            tomTomPositions.add(position.asTomTomRoutePosition());
        }
        return new TomTomRoute(format, getCharacteristics(), getName(), tomTomPositions);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CsvRoute csvRoute = (CsvRoute) o;

        return (name != null ? name.equals(csvRoute.name) : csvRoute.name == null) &&
                (positions != null ? positions.equals(csvRoute.positions) : csvRoute.positions == null);
    }

    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (positions != null ? positions.hashCode() : 0);
        return result;
    }
}
