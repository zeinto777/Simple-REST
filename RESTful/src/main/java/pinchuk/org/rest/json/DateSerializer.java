package pinchuk.org.rest.json;


import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Andrii Pinchuk on 27.02.2017.
 */
public class DateSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = simpleDateFormat.format(localDate);
        jsonGenerator.writeString(dateString);
    }

}
