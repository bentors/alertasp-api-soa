package br.com.fiap.alertasp.shared;

import java.util.List;

public final class GeoUtils {

    private static final double RAIO_TERRA_METROS = 6_371_000;
    private static final double METROS_POR_GRAU_LAT = 110_540;
    private static final double METROS_POR_GRAU_LNG_EQUADOR = 111_320;

    private GeoUtils() {
    }

    /** Distância haversine em metros. */
    public static double distanciaMetros(Coordenada a, Coordenada b) {
        double dLat = Math.toRadians(b.latitude() - a.latitude());
        double dLng = Math.toRadians(b.longitude() - a.longitude());
        double h = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(Math.toRadians(a.latitude())) * Math.cos(Math.toRadians(b.latitude()))
                * Math.pow(Math.sin(dLng / 2), 2);
        return 2 * RAIO_TERRA_METROS * Math.asin(Math.sqrt(h));
    }

    /** Menor distância em metros entre o ponto e a polilinha (trajeto). */
    public static double distanciaAteLinhaMetros(Coordenada p, List<Coordenada> linha) {
        if (linha.size() == 1) {
            return distanciaMetros(p, linha.get(0));
        }
        double menor = Double.MAX_VALUE;
        for (int i = 0; i < linha.size() - 1; i++) {
            menor = Math.min(menor, distanciaAteSegmento(p, linha.get(i), linha.get(i + 1)));
        }
        return menor;
    }

    // ponytail: projeção equiretangular local em torno de p, precisa o bastante na escala de uma cidade
    private static double distanciaAteSegmento(Coordenada p, Coordenada a, Coordenada b) {
        double cosLat = Math.cos(Math.toRadians(p.latitude()));
        double ax = (a.longitude() - p.longitude()) * cosLat * METROS_POR_GRAU_LNG_EQUADOR;
        double ay = (a.latitude() - p.latitude()) * METROS_POR_GRAU_LAT;
        double bx = (b.longitude() - p.longitude()) * cosLat * METROS_POR_GRAU_LNG_EQUADOR;
        double by = (b.latitude() - p.latitude()) * METROS_POR_GRAU_LAT;
        double dx = bx - ax;
        double dy = by - ay;
        double compr2 = dx * dx + dy * dy;
        double t = compr2 == 0 ? 0 : Math.max(0, Math.min(1, -(ax * dx + ay * dy) / compr2));
        double x = ax + t * dx;
        double y = ay + t * dy;
        return Math.sqrt(x * x + y * y);
    }
}
