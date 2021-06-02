package universitymanagement.CTIservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsConsumer {
    @Autowired
    StudentRepository studentRepository;

    @JmsListener(destination = "${student.jms.destination}")
    public void consumeMessage(String data)  {

        try {

            ObjectMapper mapper = new ObjectMapper();
            //Json data to Product object
           Student student = mapper.readValue(data,Student.class);
            studentRepository.save(student);

        } catch (JsonProcessingException e){
            e.getStackTrace();
        }
    }
}
