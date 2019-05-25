var getLayer = function (url, attrib) {
    return L.tileLayer(url, {maxZoom: 18, attribution: attrib});
};
var Layers = {
    mapBox: {
        azavea: 'https://{s}.tiles.mapbox.com/v3/azavea.map-zbompf85/{z}/{x}/{y}.png',
        worldLight: 'https://c.tiles.mapbox.com/v3/mapbox.world-light/{z}/{x}/{y}.png',
        attrib: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery &copy; <a href="http://mapbox.com">MapBox</a>'
    }
}

var map = (function () {
    var selected = getLayer(Layers.mapBox.azavea, Layers.mapBox.attrib);
    var baseLayers = {
        "Default": selected
    };
    var m = L.map('map');

    m.setView([39.8, 116.6], 9);

    selected.addTo(m);

    m.lc = L.control.layers(baseLayers).addTo(m);
    return m;
})()

// var drawnItems = new L.FeatureGroup();
// map.addLayer(drawnItems);
// var drawControl = new L.Control.Draw({
//     edit: {
//         featureGroup: drawnItems
//     }
// });
// map.addControl(drawControl);

var summary = (function () {
    var polygon = null;
    var layers = {};
    return {
        getPolygon: function () {
            return polygon;
        },
        setPolygon: function (p) {
            polygon = p;
            // weightedOverlay.update();
            // update(true);
        },
        clear: function () {
            if (polygon) {
                drawing.clear(polygon);
                polygon = null;
            }
        }
    };
})();


var setupSize = function () {
    var bottomPadding = 10;

    var resize = function () {
        map.invalidateSize();
    };
    resize();
    $(window).resize(resize);
};

var polygon = null;
var drawing = (function () {
    var drawnItems = new L.FeatureGroup();
    map.addLayer(drawnItems);

    var drawOptions = {
        draw: {
            position: 'topleft',
            marker: false,
            polyline: false,
            rectangle: false,
            circle: false,
            polygon: {
                title: 'Draw a polygon for summary information.',
                allowIntersection: false,
                drawError: {
                    color: '#b00b00',
                    timeout: 1000
                },
                shapeOptions: {
                    color: '#338FF2'
                }
            },
        },
        edit: false
    };

    var drawControl = new L.Control.Draw(drawOptions);
    map.addControl(drawControl);

    map.on('draw:created', function (e) {
        if (e.layerType === 'polygon') {
            summary.setPolygon(e.layer);
        }
    });

    map.on('draw:edited', function (e) {
        var polygon = summary.getPolygon();
        if (polygon != null) {
            // summary.update();
            // weightedOverlay.update();
        }
    });

    map.on('draw:drawstart', function (e) {
        var polygon = summary.getPolygon();
        if(polygon != null) { drawnItems.removeLayer(polygon); }
    });

    map.on('draw:drawstop', function (e) {
        if(summary.getPolygon())
        {
            drawnItems.addLayer(summary.getPolygon());
            console.log(GJ.fromPolygon(summary.getPolygon()))
        }
    });

    return {
        clear: function (polygon) {
            drawnItems.removeLayer(polygon);
        }
    }
})();


// On page load
$(document).ready(function () {
    $('#clearButton').click(function () {
        summary.clear();
        return false;
    });
    setupSize();
});
// //create the tile layers
// var ndvi = new L.tileLayer("http://192.168.1.106:8080/ndvi/{z}/{x}/{y}", {maxNativeZoom: 13, maxZoom: 18});
// var ndwi = new L.tileLayer("http://localhost:8080/ndwi/{z}/{x}/{y}", {maxNativeZoom: 13, maxZoom: 18});
// var base = new L.tileLayer("http://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}.png");
// //link leaflet map with html
// var map = L.map('map');
// map.setView([39.6057063820, 116.4990393689], 7);
// //create an overlay object
// var overlayMaps = {
//     "NDVI": ndvi,
//     "NDWI": ndwi
// };
// //add the layers to the map
// L.control.layers(overlayMaps, {}, {collapsed: false}).addTo(map);
// base.addTo(map);
// overlayMaps.NDVI.addTo(map);