// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.mathlang.impl;

import java.io.IOException;

import com.google.codeu.mathlang.core.tokens.Token;
import com.google.codeu.mathlang.parsing.TokenReader;

// MY TOKEN READER
//
// This is YOUR implementation of the token reader interface. To know how
// it should work, read src/com/google/codeu/mathlang/parsing/TokenReader.java.
// You should not need to change any other files to get your token reader to
// work with the test of the system.
public final class MyTokenReader implements TokenReader {

  private int index;
  private String stream;

  public MyTokenReader(String source) {
    this.index = 0;
    this.stream = source;
  }

  @Override
  public Token next() throws IOException {

    if (index >= stream.length()) {
      return null;
    }
    else {
      String tempString = stream.substring(index);
      char firstCharacter = tempString.charAt(0);

      // Tokenizing symbol characters
      if (firstCharacter == '+' || firstCharacter == '-' || firstCharacter == '/' || firstCharacter == '=') {
        this.index++;
        return new SymbolToken(firstCharacter);
      }

      //Tokenizing numbers
      else if (isNumber(firstCharacter)) {
        String number = Character.toString(firstCharacter);
        int i = 1;
        while ((i < tempString.length()) && (isNumber(tempString.charAt(i)) || tempString.charAt(i) == '.')) {
          number += tempString.charAt(i);
        }
        double doubleNum = Double.parseDouble(number);
        this.index++;
        return new NumberToken(doubleNum);
      }

      //Tokenizing commments (aka strings) 
      else if (firstCharacter == '\"') {
        int endIndex = tempString.indexOf('\"', 1);
        if (endIndex > 0) {
          this.index = endIndex + 1;
          return new StringToken(tempString.substring(1, endIndex));
        }
        else {
          throw new IOException("Need to close string for valid tokenized input");
        }
      }

      //Tokenizing names
      else {
        String name = Character.toString(firstCharacter);
        for (int i = 1; Character.isAlphabetic(tempString.charAt(i)); i++) {
          name += tempString.charAt(i);
          index++;
        }
        return new NameToken(name);
      }
    }

    return null;
  }

  public boolean isNumber(char c) {
    try {
      Integer.parseInt(Character.toString(c));
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }

}
