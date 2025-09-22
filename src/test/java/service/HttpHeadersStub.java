package service;

import java.net.http.HttpHeaders;
import java.util.*;

public class HttpHeadersStub {
  public static HttpHeaders withLocation(String location) {
    Map<String, List<String>> map = new HashMap<>();
    map.put("Location", List.of(location));
    return HttpHeaders.of(map, (s1, s2) -> true);
  }
}
