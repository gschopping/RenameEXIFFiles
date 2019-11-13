package EXIFFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WriteEXIF {
    private String mediaFile;
    private String fileType;
    private List<String> arguments;

    public WriteEXIF(String mediaFile, boolean removeOriginal) throws IOException {
        this.mediaFile = mediaFile;
        this.fileType = this.getFileType();
        this.arguments = new ArrayList<String>();
        if (removeOriginal) {
            this.arguments.add("-overwrite_original");
        }
    }


    private void writeArguments() throws IOException {
        String tempdir =  System.getProperty("java.io.tmpdir");
        FileWriter fileWriter = new FileWriter(tempdir + "arguments.txt");
        for (String argument : this.arguments) {
            fileWriter.write(argument + "\n");
        }
        fileWriter.close();
    }

    private String ISO3Code(String countryCode) {
        Locale obj = new Locale("", countryCode);
        return obj.getISO3Country();
    }

    private String getFileType() throws IOException {
        String fileType;
        Process process = Runtime.getRuntime().exec("exiftool.bat -s3 -File:FileType \"" + this.mediaFile + "\"");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        fileType = reader.readLine();
        reader.close();
        return fileType;
    }

    private void writeTag(String[] tags, String value) throws IOException {
        for (String tag : tags) {
            this.arguments.add("-" + tag + "=" + value);
        }
    }

    private void writeKeys(String[] tags, String[] values) throws IOException {
        for (String value : values) {
            for (String tag : tags) {
                this.arguments.add("-" + tag + "=" + value);
            }
        }
    }

    public void writeFile(String writeFile, boolean overWrite) throws IOException {
        if (! this.fileType.equals("M2TS")) {
            String result;
            String tempdir =  System.getProperty("java.io.tmpdir");
            writeArguments();
            // Before copting first delete original file
            File file = new File(writeFile);
            if (overWrite) {
                if (file.exists()) {
                    if (! file.delete()) throw new IOException(String.format("%s can't be deleted", file.getPath()));
                }
            }
            else {
                if (file.exists()) throw new IOException(String.format("%s already exists", file.getPath()));
            }
            Process process = Runtime.getRuntime().exec("exiftool.bat -charset IPTC=UTF8 -@ " + tempdir + "arguments.txt \"" + this.mediaFile + "\" -o \"" + writeFile + "\"");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            // read result first line
            result = reader.readLine();
            if ((result != null) && (! result.matches("\\s*1 image files (created|updated)"))) {
                // maybe correct answer on second line
                result = reader.readLine();
                if ((result != null) && (! result.matches( "\\s*1 image files (created|updated)"))) {
                    throw new IOException("Update of file " + writeFile + " failed");
                }
            }
            reader.close();
        }
    }

    public void setAuthor(String author) throws IOException {
        if (! author.isEmpty()) {
            String[] tags = new String[6];
            tags[0] = "EXIF:Artist";
            tags[1] = "EXIF:XPAuthor";
            tags[2] = "XMP:Creator";
            tags[3] = "XMP:CaptionWriter";
            tags[4] = "IPTC:By-line";
            tags[5] = "IPTC:Writer-Editor";
            writeTag(tags, author);
        }
    }

    public void setCopyright(String copyright) throws IOException {
        if (! copyright.isEmpty()) {
            String[] tags = new String[3];
            tags[0] = "EXIF:Copyright";
            tags[1] = "XMP:Rights";
            tags[2] = "IPTC:CopyrightNotice";
            writeTag(tags, copyright);
        }
    }

    public void setComment(String comment) throws IOException {
        if (! comment.isEmpty()) {
            String[] tags = new String[2];
            tags[0] = "EXIF:UserComment";
            tags[1] = "EXIF:XPComment";
            writeTag(tags, comment);
        }
    }

    public void setCountryCode(String countryCode) throws IOException {
        if (! countryCode.isEmpty()) {
            String[] tags = new String[1];
            tags[0] = "XMP:CountryCode";
            writeTag(tags, countryCode);
            tags[0] = "IPTC:Country-PrimaryLocationCode";
            writeTag(tags, this.ISO3Code(countryCode));
        }
    }

    public void setCountry(String country) throws IOException {
        if (! country.isEmpty()) {
            String[] tags = new String[2];
            tags[0] = "XMP:Country";
            tags[1] = "IPTC:Country-PrimaryLocationName";
            writeTag(tags, country);
        }
    }

    public void setProvince(String province) throws IOException {
        if (! province.isEmpty()) {
            String[] tags = new String[2];
            tags[0] = "XMP:State";
            tags[1] = "IPTC:Province-State";
            writeTag(tags, province);
        }
    }

    public void setCity(String city) throws IOException {
        if (! city.isEmpty()) {
            String[] tags = new String[1];
            tags[0] = "XMP:City";
            writeTag(tags, city);
        }
    }

    public void setLocation(String location) throws IOException {
        if (! location.isEmpty()) {
            String[] tags = new String[2];
            tags[0] = "IPTC:Sub-location";
            tags[1] = "IPTC:ObjectName";
            writeTag(tags, location);
        }
    }

    public void setTitle(String title) throws IOException {
        if (! title.isEmpty()) {
            String[] tags = new String[4];
            tags[0] = "XMP:Title";
            tags[1] = "EXIF:ImageDescription";
            tags[2] = "EXIF:XPTitle";
            tags[3] = "XMP:Description";
            writeTag(tags, title);
        }
    }

    public void setURL(String url) throws IOException {
        if (! url.isEmpty()) {
            String[] tags = new String[2];
            tags[0] = "XMP:BaseURL";
            tags[1] = "Photoshop:URL";
            writeTag(tags, url);
        }
    }

    public void setDescription(String description) throws IOException {
        if (! description.isEmpty()) {
            String[] tags = new String[3];
            tags[0] = "XMP:Headline";
            tags[1] = "EXIF:XPSubject";
            tags[2] = "IPTC:Caption-Abstract";
            writeTag(tags, description);
        }
    }

    public void setKeys(String[] keys) throws IOException {
        if (keys.length > 0) {
            String[] tags = new String[4];
            tags[0] = "XMP:Subject";
            tags[1] = "IPTC:Keywords";
            tags[2] = "XMP:LastKeywordXMP";
            tags[3] = "XMP:LastKeywordIPTC";
            writeKeys(tags, keys);
            String[] tags2 = new String[1];
            String value = "";
            for (String key : keys) {
                if (value.isEmpty()) {
                    value = key;
                } else {
                    value = value + ", " + key;
                }
            }
            tags2[0] = "EXIF:XPKeywords";
            writeTag(tags2, value);
        }
    }

    public void setSpecialInstructions(String specialInstructions) throws IOException {
        if (! specialInstructions.isEmpty()) {
            String[] tags = new String[2];
            tags[0] = "IPTC:SpecialInstructions";
            tags[1] = "XMP:Instructions";
            writeTag(tags, specialInstructions);
        }
    }

    public void setAddress(Map<String, String> address) throws IOException {
        setCountryCode(address.get("countrycode"));
        setCountry(address.get("country"));
        setCity(address.get("city"));
        setProvince(address.get("province"));
        setLocation(address.get("location"));

    }

}
