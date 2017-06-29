/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.solr.schema;

import org.apache.solr.core.AbstractBadConfigTestBase;

public class BadIndexSchemaTest extends AbstractBadConfigTestBase {

  private void doTest(final String schema, final String errString) 
    throws Exception {
    assertConfigs("solrconfig-basic.xml", schema, errString);
  }

  public void testSevereErrorsForInvalidFieldOptions() throws Exception {
    doTest("bad-schema-not-indexed-but-norms.xml", "bad_field");
    doTest("bad-schema-not-indexed-but-tf.xml", "bad_field");
    doTest("bad-schema-not-indexed-but-pos.xml", "bad_field");
    doTest("bad-schema-omit-tf-but-not-pos.xml", "bad_field");
  }

  public void testSevereErrorsForDuplicateFields() throws Exception {
    doTest("bad-schema-dup-field.xml", "fAgain");
  }

  public void testSevereErrorsForDuplicateDynamicField() throws Exception {
    doTest("bad-schema-dup-dynamicField.xml", "_twice");
  }
  public void testSevereErrorsForUnsupportedAttributesOnDynamicField() throws Exception {
    doTest("bad-schema-dynamicfield-default-val.xml", "default");
    doTest("bad-schema-dynamicfield-required.xml", "required");
  }

  public void testSevereErrorsForDuplicateFieldType() throws Exception {
    doTest("bad-schema-dup-fieldType.xml", "ftAgain");
  }

  public void testSevereErrorsForUnexpectedAnalyzer() throws Exception {
    doTest("bad-schema-nontext-analyzer.xml", "StrField (bad_type)");
    doTest("bad-schema-analyzer-class-and-nested.xml", "bad_type");
  }

  public void testBadExternalFileField() throws Exception {
    doTest("bad-schema-external-filefield.xml",
           "Only float (TrieFloatField) is currently supported as external field type.");
  }

  public void testUniqueKeyRules() throws Exception {
    doTest("bad-schema-uniquekey-is-copyfield-dest.xml", 
           "can not be the dest of a copyField");
    doTest("bad-schema-uniquekey-uses-default.xml", 
           "can not be configured with a default value");
    doTest("bad-schema-uniquekey-multivalued.xml", 
           "can not be configured to be multivalued");
    doTest("bad-schema-uniquekey-uses-points.xml", 
           "can not be configured to use a Points based FieldType");
  }

  public void testMultivaluedCurrency() throws Exception {
    doTest("bad-schema-currency-ft-multivalued.xml", 
           "types can not be multiValued: currency");
    doTest("bad-schema-currency-multivalued.xml", 
           "fields can not be multiValued: money");
    doTest("bad-schema-currency-dynamic-multivalued.xml", 
           "fields can not be multiValued: *_c");
    doTest("bad-schema-currencyfieldtype-ft-multivalued.xml",
        "types can not be multiValued: currency");
    doTest("bad-schema-currencyfieldtype-multivalued.xml",
        "fields can not be multiValued: money");
    doTest("bad-schema-currencyfieldtype-dynamic-multivalued.xml",
        "fields can not be multiValued: *_c");
  }

  public void testCurrencyOERNoRates() throws Exception {
    doTest("bad-schema-currency-ft-oer-norates.xml", 
           "ratesFileLocation");
    doTest("bad-schema-currencyfieldtype-ft-oer-norates.xml",
        "ratesFileLocation");
  }

  public void testCurrencyBogusCode() throws Exception {
    doTest("bad-schema-currency-ft-bogus-default-code.xml", 
           "HOSS");
    doTest("bad-schema-currency-ft-bogus-code-in-xml.xml", 
           "HOSS");
    doTest("bad-schema-currencyfieldtype-ft-bogus-default-code.xml",
        "HOSS");
    doTest("bad-schema-currencyfieldtype-ft-bogus-code-in-xml.xml",
        "HOSS");
  }
  
  public void testCurrencyDisallowedSuffixParams() throws Exception {
    doTest("bad-schema-currency-ft-code-suffix.xml", 
        "Unknown parameter(s)");
    doTest("bad-schema-currency-ft-amount-suffix.xml",
        "Unknown parameter(s)");
  }
  
  public void testCurrencyBogusSuffixes() throws Exception {
    doTest("bad-schema-currencyfieldtype-bogus-code-suffix.xml",
           "Undefined dynamic field for codeStrSuffix");
    doTest("bad-schema-currencyfieldtype-bogus-amount-suffix.xml",
           "Undefined dynamic field for amountLongSuffix");
    doTest("bad-schema-currencyfieldtype-wrong-code-ft.xml",
           "Dynamic field for codeStrSuffix=\"_l\" must have type class of (or extending) StrField");
    doTest("bad-schema-currencyfieldtype-wrong-amount-ft.xml",
           "Dynamic field for amountLongSuffix=\"_s\" must have type class extending LongValueFieldType");
  } 
  
  public void testCurrencyMissingSuffixes() throws Exception {
    doTest("bad-schema-currencyfieldtype-missing-code-suffix.xml",
        "Missing required param codeStrSuffix");
    doTest("bad-schema-currencyfieldtype-missing-amount-suffix.xml",
        "Missing required param amountLongSuffix");
  }

  public void testPerFieldtypeSimButNoSchemaSimFactory() throws Exception {
    doTest("bad-schema-sim-global-vs-ft-mismatch.xml", "global similarity does not support it");
  }
  
  public void testPerFieldtypePostingsFormatButNoSchemaCodecFactory() throws Exception {
    doTest("bad-schema-codec-global-vs-ft-mismatch.xml", "codec does not support");
  }

  public void testDocValuesUnsupported() throws Exception {
    doTest("bad-schema-unsupported-docValues.xml", "does not support doc values");
  }

  public void testSweetSpotSimBadConfig() throws Exception {
    doTest("bad-schema-sweetspot-both-tf.xml", "Can not mix");
    doTest("bad-schema-sweetspot-partial-baseline.xml", 
           "Overriding default baselineTf");
    doTest("bad-schema-sweetspot-partial-hyperbolic.xml", 
           "Overriding default hyperbolicTf");
    doTest("bad-schema-sweetspot-partial-norms.xml", 
           "Overriding default lengthNorm");
  }
  
  public void testBogusParameters() throws Exception {
    doTest("bad-schema-bogus-field-parameters.xml", "Invalid field property");
  }
  
  public void testBogusAnalysisParameters() throws Exception {
    doTest("bad-schema-bogus-analysis-parameters.xml", "Unknown parameters");
  }

  public void testSimDefaultFieldTypeHasNoExplicitSim() throws Exception {
    doTest("bad-schema-sim-default-has-no-explicit-sim.xml",
           "ft-has-no-sim");
  }
  
  public void testSimDefaultFieldTypeDoesNotExist() throws Exception {
    doTest("bad-schema-sim-default-does-not-exist.xml",
           "ft-does-not-exist");
  }

  public void testDefaultOperatorBanned() throws Exception {
    doTest("bad-schema-default-operator.xml",
           "default operator in schema (solrQueryParser/@defaultOperator) not supported");
  }

  public void testDefaultOperatorNotBannedForPre66() throws Exception {
    initCore("solrconfig-minimal-version6.xml", "bad-schema-default-operator.xml");
    deleteCore();
  }

  public void testSchemaWithDefaultSearchField() throws Exception {
    doTest("bad-schema-defaultsearchfield.xml", "defaultSearchField");
  }

  public void testDefaultSearchFieldNotBannedForPre66() throws Exception {
    initCore("solrconfig-minimal-version6.xml", "bad-schema-defaultsearchfield.xml");
    deleteCore();
  }
}
