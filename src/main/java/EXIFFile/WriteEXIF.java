package EXIFFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WriteEXIF {
    private String mediaFile;
    private String copyFile;
    private String fileType;
    private List<String> arguments;

    public WriteEXIF(String mediaFile, String writeFile) throws IOException {
        this.mediaFile = mediaFile;
        this.copyFile = writeFile;
        this.fileType = this.GetFileType();
        this.arguments = new ArrayList<String>();
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

    private String GetFileType() throws IOException {
        String fileType;
        Process process = Runtime.getRuntime().exec("exiftool.bat -s3 -File:FileType " + this.mediaFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        fileType = reader.readLine();
        reader.close();
        return fileType;
    }

    private void WriteTag(String[] tags, String value) throws IOException {
        for (String tag : tags) {
            this.arguments.add("-" + tag + "=" + value);
        }
    }

    private void WriteKeys(String[] tags, String[] values) throws IOException {
        for (String value : values) {
            for (String tag : tags) {
                this.arguments.add("-" + tag + "=" + value);
            }
        }
    }

    public void WriteFile(boolean overWrite) throws IOException {
        if (! this.fileType.equals("M2TS")) {
            String result;
            String tempdir =  System.getProperty("java.io.tmpdir");
            writeArguments();
            // Before copting first delete original file
            if (overWrite) {
                try {
                    File file = new File(this.copyFile);
                    file.delete();
                }
                catch (Exception e) {
                }
            }
            Process process = Runtime.getRuntime().exec("exiftool.bat -charset IPTC=UTF8 -@ " + tempdir + "arguments.txt " + this.mediaFile + " -o " + this.copyFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            result = reader.readLine();
            if (result != null) {
                if (! result.matches("\\s*1 image files created")) {
                    throw new IOException("Update of file " + this.mediaFile + " failed");
                }
            }
            reader.close();
            // set to inital value in case you want to write new tags to the same file
            this.arguments.clear();
        }
    }

    public void SetAuthor(String author) throws IOException {
        String[] tags = new String[6];
        tags[0] = "EXIF:Artist";
        tags[1] = "EXIF:XPAuthor";
        tags[2] = "XMP:Creator";
        tags[3] = "XMP:CaptionWriter";
        tags[4] = "IPTC:By-line";
        tags[5] = "IPTC:Writer-Editor";
        WriteTag(tags, author);
    }

    public void SetCopyright(String copyright) throws IOException {
        String[] tags = new String[3];
        tags[0] = "EXIF:Copyright";
        tags[1] = "XMP:Rights";
        tags[2] = "IPTC:CopyrightNotice";
        WriteTag(tags, copyright);
    }

    public void SetComment(String comment) throws IOException {
        String[] tags = new String[2];
        tags[0] = "EXIF:UserComment";
        tags[1] = "EXIF:XPComment";
        WriteTag(tags, comment);
    }

    public void SetCountryCode(String countryCode) throws IOException {
        String[] tags = new String[1];
        tags[0] = "XMP:CountryCode";
        WriteTag(tags, countryCode);
        tags[0] = "IPTC:Country-PrimaryLocationCode";
        WriteTag(tags, this.ISO3Code(countryCode));
    }

    public void SetCountry(String country) throws IOException {
        String[] tags = new String[2];
        tags[0] = "XMP:Country";
        tags[1] = "IPTC:Country-PrimaryLocationName";
        WriteTag(tags, country);
    }

    public void SetProvince(String province) throws IOException {
        String[] tags = new String[2];
        tags[0] = "XMP:State";
        tags[1] = "IPTC:Province-State";
        WriteTag(tags, province);
    }

    public void SetCity(String city) throws IOException {
        String[] tags = new String[1];
        tags[0] = "XMP:City";
        WriteTag(tags, city);
    }

    public void SetLocation(String location) throws IOException {
        String[] tags = new String[2];
        tags[0] = "IPTC:Sub-location";
        tags[1] = "IPTC:ObjectName";
        WriteTag(tags, location);
    }

    public void SetTitle(String title) throws IOException {
        String[] tags = new String[4];
        tags[0] = "XMP:Title";
        tags[1] = "EXIF:ImageDescription";
        tags[2] = "EXIF:XPTitle";
        tags[3] = "XMP:Description";
        WriteTag(tags, title);
    }

    public void SetURL(String url) throws IOException {
        String[] tags = new String[2];
        tags[0] = "XMP:BaseURL";
        tags[1] = "Photoshop:URL";
        WriteTag(tags, url);
    }

    public void SetDescription(String description) throws IOException {
        String[] tags = new String[3];
        tags[0] = "XMP:Headline";
        tags[1] = "EXIF:XPSubject";
        tags[2] = "IPTC:Caption-Abstract";
        WriteTag(tags, description);
    }

    public void SetKeys(String[] keys) throws IOException {
        String[] tags = new String[4];
        tags[0] = "XMP:Subject";
        tags[1] = "IPTC:Keywords";
        tags[2] = "XMP:LastKeywordXMP";
        tags[3] = "XMP:LastKeywordIPTC";
        WriteKeys(tags, keys);
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
        WriteTag(tags2, value);
    }

    public void SetSpecialInstructions(String specialInstructions) throws IOException {
        String[] tags = new String[2];
        tags[0] = "IPTC:SpecialInstructions";
        tags[1] = "XMP:Instructions";
        WriteTag(tags, specialInstructions);
    }




}
