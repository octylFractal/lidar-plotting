package org.rivierarobotics;

import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSV {

    private List<String> header;
    private List<List<? extends Object>> data = new ArrayList<>();

    public CSV setHeader(String... header) {
        return setHeader(Arrays.asList(header));
    }

    public CSV setHeader(List<String> header) {
        this.header = header;
        return this;
    }

    public CSV setData(List<List<? extends Object>> data) {
        this.data = data;
        return this;
    }

    public CSV addLine(List<? extends Object> line) {
        checkState(line.size() == this.header.size(),
                "line must be the size of header");
        data.add(line);
        return this;
    }

    public CSV clearData() {
        data.clear();
        return this;
    }

    public void writeTo(Writer writer) throws IOException {
        List<List<? extends Object>> data = this.data;
        data.add(0, header);
        for (List<? extends Object> list : data) {
            writer.write(format(list));
            writer.write("\n");
        }
    }

    private String format(List<? extends Object> list) {
        List<String> out = new ArrayList<>(list.size());
        for (Object object : list) {
            if (object instanceof Number) {
                // output raw number
                out.add(object.toString());
            } else {
                // double up quotes
                String s = object.toString();
                s.replace("\"", "\"\"");
                out.add(s);
            }
        }
        // separate with '","', with the needed quotes on the start and end
        return out.stream().collect(Collectors.joining("\",\"", "\"", "\""));
    }

}
