package org.example.shop.validators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.shop.DTO.requests.V1QueryOrdersRequest;
import org.springframework.stereotype.Component;

@Component
public class V1QueryOrdersRequestValidator {

  public Map<String, List<String>> validate(V1QueryOrdersRequest request) {
    Map<String, List<String>> errors = new HashMap<>();

    if (request == null) {
      errors.put("Request", List.of("Request cannot be null"));
      return errors;
    }

    if (request.getLimit() != null && request.getLimit() < 0) {
      errors.put("Limit", List.of("Limit must be non-negative"));
    }

    if (request.getOffset() != null && request.getOffset() < 0) {
      errors.put("Offset", List.of("Offset must be non-negative"));
    }

    if (request.getIds() != null) {
      for (int i = 0; i < request.getIds().length; i++) {
        if (request.getIds()[i] == null || request.getIds()[i] <= 0) {
          errors.put("Ids[" + i + "]", List.of("Id must be greater than 0"));
        }
      }
    }

    if (request.getCustomerIds() != null) {
      for (int i = 0; i < request.getCustomerIds().length; i++) {
        if (request.getCustomerIds()[i] == null || request.getCustomerIds()[i] <= 0) {
          errors.put("CustomerIds[" + i + "]", List.of("CustomerId must be greater than 0"));
        }
      }
    }

    return errors;
  }
}

