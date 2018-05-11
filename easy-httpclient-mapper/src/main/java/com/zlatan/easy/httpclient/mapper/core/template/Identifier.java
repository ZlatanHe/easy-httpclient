package com.zlatan.easy.httpclient.mapper.core.template;

import com.zlatan.easy.httpclient.core.util.ArgValidationUtils;
import com.zlatan.easy.httpclient.mapper.exception.HttpClientMapperBuildException;
import lombok.Getter;

import java.util.List;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 10/05/2018 10:56 AM
 */
@Getter
public final class Identifier {

  /********************************
   * Static methods and fields
   *
   *******************************/

  public static String findIdentifiers(String target, List<Identifier> identifiers)
          throws HttpClientMapperBuildException {
    ArgValidationUtils.validateObjectNotNull("identifiers", identifiers);
    boolean hit = false;
    StringBuilder resultBuilder = new StringBuilder();
    StringBuilder identifierBuilder = new StringBuilder();
    for (int i = 0; i < target.length(); i++) {
      char ch = target.charAt(i);
      if (ch == '{') {
        hit = true;
        identifierBuilder = new StringBuilder();
        continue;
      }
      if (!hit) {
        resultBuilder.append(String.valueOf(ch));
        continue;
      }
      if (ch == '}') {
        if (identifierBuilder.length() == 0) {
          throw new HttpClientMapperBuildException(String.format(
                  "Bad identifier with empty value. Target=%s",
                  target
          ));
        }
        resultBuilder.append("%s");
        identifiers.add(new Identifier(identifierBuilder.toString()));
        hit = false;
        identifierBuilder = null;
        continue;
      }
      identifierBuilder.append(String.valueOf(ch));
    }
    return resultBuilder.toString();
  }

  /********************************
   * Instance methods and fields
   *
   *******************************/

  private final String key;

  private Identifier(String key) {
    this.key = key;
  }
}
