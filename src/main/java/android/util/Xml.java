package android.util;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class Xml {
    public static XmlPullParser newPullParser() {
        try {
            //Android uses the same pull parser, that version is slightly modified, though...
            KXmlParser parser = new KXmlParser() {
                @Override
                public void setFeature(String feature, boolean value) throws XmlPullParserException {
                    if (XmlPullParser.FEATURE_PROCESS_DOCDECL.equals(feature)) {
                        //This version doesn't support the DOCDECL feature, so we skip it
                        return;
                    }

                    super.setFeature(feature, value);
                }
            };
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            return parser;
        } catch (XmlPullParserException e) {
            throw new RuntimeException("XML parsing error", e);
        }
    }
}