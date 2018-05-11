package com.zlatan.easy.httpclient.mapper.core.template;

import com.google.common.collect.Sets;
import com.zlatan.easy.httpclient.core.constant.TypeCache;
import com.zlatan.easy.httpclient.core.request.encode.RequestBodyEncoder;
import com.zlatan.easy.httpclient.core.response.decode.ResponseBodyDecoder;
import com.zlatan.easy.httpclient.core.response.decode.SimpleStringBodyDecoder;
import com.zlatan.easy.httpclient.mapper.annotations.DirectUrlHttpRequest;
import com.zlatan.easy.httpclient.mapper.annotations.HttpRequest;
import com.zlatan.easy.httpclient.mapper.annotations.ResponseBody;
import com.zlatan.easy.httpclient.mapper.annotations.element.*;
import com.zlatan.easy.httpclient.mapper.constant.RequestMethod;
import com.zlatan.easy.httpclient.mapper.exception.HttpClientMapperBuildException;
import com.zlatan.easy.httpclient.mapper.util.BiFunction;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Title: Factory class of {@link InterfaceTemplate}
 * @Description: Process the request model represent in an instance of
 * {@link HttpRequest} or {@link DirectUrlHttpRequest}
 *
 * @Date: Created By hewei in 09/05/2018 7:01 PM
 */
@SuppressWarnings("unchecked")
public final class RequestTemplateFactory {

  public static InterfaceTemplate buildTemplate(Method method)
          throws HttpClientMapperBuildException {
    try {
      InterfaceTemplate template =
              isFullModeled(method) ? buildModeledTemplate(method) : buildDirectTemplate(method);
      Class<? extends ResponseBodyDecoder> responseBodyDecoderClass =
              extractDecoderClass(method);
      Class<?> returnClass = extractReturnClass(method);
      Map<String, String> staticHeaders = extractStaticHeaders(method);
      List<RequestConfigItem> itemConfigList =
              extractItemConfigs(method.getParameters(), template);
      return template
              .setStaticHeaders(staticHeaders)
              .setResponseBodyDecoderClass(responseBodyDecoderClass)
              .setReturnClass(returnClass)
              .setItemConfigList(itemConfigList)
              .checkProxyRequirement(method);
    } catch (Exception e) {
      throw new HttpClientMapperBuildException(String.format("Method is %s", method.toString()), e);
    }
  }

  /**
   * Check the annotation of the request method. If necessary annotation is absent,
   * {@link HttpClientMapperBuildException} is thrown.
   * When {@link HttpRequest} is present, return true;
   * When {@link DirectUrlHttpRequest} is present, return false;
   * If both are present, throw {@link HttpClientMapperBuildException}.
   *
   * @throws HttpClientMapperBuildException
   */
  private static boolean isFullModeled(Method method)
          throws HttpClientMapperBuildException {
    boolean annotationPresent = false;
    boolean isFullModeled = false;
    if (method.isAnnotationPresent(HttpRequest.class)) {
      isFullModeled = true;
      annotationPresent = true;
    }
    if (method.isAnnotationPresent(DirectUrlHttpRequest.class)) {
      if (annotationPresent) {
        throw new HttpClientMapperBuildException(String.format(
                "Duplicated request definition! Method is %s",
                method.toString()
        ));
      }
      annotationPresent = true;
    }
    if (!annotationPresent) {
      throw new HttpClientMapperBuildException(String.format(
              "No request definition! Method is %s",
              method.toString()
      ));
    }
    return isFullModeled;
  }

  /**
   * Build instance of {@link ModeledInterfaceTemplate}
   *
   * @throws HttpClientMapperBuildException
   */
  private static InterfaceTemplate buildModeledTemplate(Method method)
          throws HttpClientMapperBuildException {
    HttpRequest annotation = method.getAnnotation(HttpRequest.class);
    boolean isGet = annotation.method().equals(RequestMethod.GET);
    boolean useSSL = annotation.useSSL();
    Class<? extends RequestBodyEncoder> encoder = annotation.encoder();
    List<Identifier> hostIdentifiers = new ArrayList<>();
    List<Identifier> pathIdentifiers = new ArrayList<>();
    String hostTemplate = Identifier.findIdentifiers(annotation.host(), hostIdentifiers);
    String pathTemplate = Identifier.findIdentifiers(annotation.path(), pathIdentifiers);
    return new ModeledInterfaceTemplate(isGet)
            .setUseSSL(useSSL)
            .setHostTemplate(hostTemplate)
            .setHostIdentifiers(hostIdentifiers)
            .setPathTemplate(pathTemplate)
            .setPathIdentifiers(pathIdentifiers)
            .setRequestBodyEncoderClass(encoder);
  }

  /**
   * Build instance of {@link DirectInterfaceTemplate}
   *
   */
  private static InterfaceTemplate buildDirectTemplate(Method method) {
    DirectUrlHttpRequest annotation = method.getAnnotation(DirectUrlHttpRequest.class);
    String urlKey = annotation.value();
    boolean isGet = annotation.method().equals(RequestMethod.GET);
    Class<? extends RequestBodyEncoder> encoder = annotation.encoder();
    return new DirectInterfaceTemplate(isGet)
            .setUrlkey(urlKey)
            .setDynamic(annotation.dynamic())
            .setRequestBodyEncoderClass(encoder);
  }

  /**
   * Extract static headers based on {@link StaticHeaders}
   *
   */
  private static Map<String, String> extractStaticHeaders(Method method) {
    Map<String, String> staticHeaders = new HashMap<>();
    if (method.isAnnotationPresent(StaticHeaders.class)) {
      StaticHeaders requestHeaders = method.getAnnotation(StaticHeaders.class);
      for (String header : requestHeaders.headers()) {
        String[] kv = header.split(":");
        if (kv.length == 2) {
          staticHeaders.put(kv[0].trim(), kv[1].trim());
        }
      }
    }
    return staticHeaders;
  }

  /**
   * Extract decoder class based on {@link ResponseBody}
   *
   * @see ResponseBodyDecoder
   */
  private static Class<? extends ResponseBodyDecoder> extractDecoderClass(Method method) {
    Class<? extends ResponseBodyDecoder> responseBodyDecoderClass = SimpleStringBodyDecoder.class;
    if (method.isAnnotationPresent(ResponseBody.class)) {
      ResponseBody responseBody = method.getAnnotation(ResponseBody.class);
      responseBodyDecoderClass = responseBody.decoder();
    }
    return responseBodyDecoderClass;
  }

  /**
   * Extract return class based on {@link ResponseBody}
   *
   */
  private static Class<?> extractReturnClass(Method method) {
    Class<?> returnClass = String.class;
    if (method.isAnnotationPresent(ResponseBody.class)) {
      ResponseBody responseBody = method.getAnnotation(ResponseBody.class);
      returnClass = responseBody.returnClass();
    }
    return returnClass;
  }

  /**
   * Extract request item config list based on annotations:
   *
   * @see Query
   * @see Path
   * @see RequestBody
   * @see RequestConfigItem
   */
  private static List<RequestConfigItem> extractItemConfigs(Parameter[] parameters,
                                                            InterfaceTemplate template)
          throws Exception {
    List<RequestConfigItem> itemConfigList = new ArrayList<>();
    if (parameters == null || parameters.length == 0) {
      return itemConfigList;
    }
    final boolean[] urlObtained =
            {(template instanceof DirectInterfaceTemplate && !((DirectInterfaceTemplate) template).isDynamic())};
    BiFunction<AnnotatedElement, Class, RequestConfigItem> constructConfigItem = (element, fieldDeclaringClass) -> {
      RequestConfigItem.ItemType itemType;
      String key = "";
      boolean isParameter = Parameter.class.isInstance(element);
      Class type = isParameter ? ((Parameter) element).getType() : ((Field) element).getType();
      boolean isPrimitive = TypeCache.fundamentalClassSet.contains(type);
      boolean urlEncodeRequired = false;
      String charsetName = StandardCharsets.UTF_8.displayName();
      Method readMethod = null;
      if (element.isAnnotationPresent(Query.class)) {
        Query query = element.getAnnotation(Query.class);
        itemType = RequestConfigItem.ItemType.QUERY;
        key = query.value();
        urlEncodeRequired = query.urlEncodeRequired();
        charsetName = query.charset();
      } else if (element.isAnnotationPresent(Path.class)) {
        Path path = element.getAnnotation(Path.class);
        itemType = RequestConfigItem.ItemType.PATH;
        key = path.value();
        urlEncodeRequired = path.urlEncodeRequired();
        charsetName = path.charset();
      } else if (element.isAnnotationPresent(Host.class)) {
        Host host = element.getAnnotation(Host.class);
        itemType = RequestConfigItem.ItemType.PATH;
        key = host.value();
      } else if (element.isAnnotationPresent(RequestBody.class)) {
        RequestBody requestBody = element.getAnnotation(RequestBody.class);
        itemType = RequestConfigItem.ItemType.BODY;
        key = requestBody.value();
        if (template.isGet()) {
          throw new HttpClientMapperBuildException("Get request is not allowed to have body params.");
        }
      } else if (element.isAnnotationPresent(Url.class)) {
        if (urlObtained[0]) {
          throw new HttpClientMapperBuildException("Duplicated url argument!");
        }
        Url url = element.getAnnotation(Url.class);
        key = url.value();
        itemType = RequestConfigItem.ItemType.URL;
        urlObtained[0] = true;
      } else if (element.isAnnotationPresent(DynamicHeader.class)) {
        DynamicHeader dynamicHeader = element.getAnnotation(DynamicHeader.class);
        itemType = RequestConfigItem.ItemType.HEADER;
        key = dynamicHeader.value();
      } else {
        // default: if GET, regarded as a query item; otherwise(POST) a body field.
        if (template.isGet()) {
          itemType = RequestConfigItem.ItemType.BODY;
        } else {
          itemType = RequestConfigItem.ItemType.QUERY;
        }
      }
      // if value is not defined, use the element name.
      if (StringUtils.isEmpty(key)) {
        if (isParameter) {
          key = ((Parameter) element).getName();
        } else {
          key = ((Field) element).getName();
        }
      }
      // Make sure the url value is valid
      if (template instanceof DirectInterfaceTemplate
              && RequestConfigItem.ItemType.URL.equals(itemType)) {
        String urlKey = ((DirectInterfaceTemplate) template).getUrlKey();
        if (!key.equals(urlKey)) {
          throw new HttpClientMapperBuildException("Url value in definition not identical to " +
                  "that in param definition.");
        }
      }
      // get read method
      if (Field.class.isInstance(element) && fieldDeclaringClass != null) {
        Field field = (Field) element;
        final Set<Class> booleanClass = Sets.newHashSet(boolean.class, Boolean.class);
        boolean isBoolean = booleanClass.contains(type);
        String methodNamePrefix = isBoolean ? "is" : "get";
        String fieldName = field.getName();
        String readMethodName = methodNamePrefix +
                String.valueOf(fieldName.charAt(0)).toUpperCase() +
                fieldName.substring(1, fieldName.length());
        readMethod = fieldDeclaringClass.getMethod(readMethodName);
      }
      return RequestConfigItem.buildNormalConfig(
              itemType,
              key,
              isPrimitive,
              urlEncodeRequired,
              charsetName,
              readMethod
      );
    };
    for (Parameter parameter : parameters) {
      if (parameter.isAnnotationPresent(RequestEntity.class)) {
        Class parameterClass = parameter.getType();
        Field[] fields = parameterClass.getDeclaredFields();
        List<RequestConfigItem> properties = new ArrayList<>();
        if (fields != null && fields.length > 0) {
          for (Field field : fields) {
            if (field.isAnnotationPresent(RequestBody.class)) {
              properties.add(constructConfigItem.apply(field, parameterClass));
            }
          }
          itemConfigList.add(RequestConfigItem.buildEntityConfig(properties));
        }
      } else {
        itemConfigList.add(constructConfigItem.apply(parameter, null));
      }
    }
    if (template instanceof DirectInterfaceTemplate && !urlObtained[0]) {
      throw new HttpClientMapperBuildException("No url value defined in the method params");
    }
    return itemConfigList;
  }
}
