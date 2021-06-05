package universitymanagement.studentservice;




import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;


    @PostMapping("/addOne")
    public Student addStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    @PostMapping("/addList")
    public List<Student> addStudentList (@RequestBody List<Student> students) {
        return studentRepository.saveAll(students);
    }

    @GetMapping("/getAll")
    public List<Student> getAllStudents () {
        return studentRepository.findAll();
    }

    @GetMapping("/deleteAll")
    public void deleteAllStudents(){studentRepository.deleteAll();}

    //Inject the jmsTemplate
    @Autowired
    private JmsTemplate jmsTemplate;

    // Set the value from the properties file
    @Value("${student.jms.destination}")
    private String jmsQueue;

    //Send a product to the message queue
    @GetMapping("/sendToCTI/{id}")
    public ResponseEntity<Student> sendToCTI(@PathVariable long id) {
        Optional<Student> student = studentRepository.findById(id);
        if(!student.isPresent()) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {

            ObjectMapper mapper = new ObjectMapper();
            //Convert the object to String
            String jsonInString = mapper.writeValueAsString(student.get());
            //Send the data to the message queue
            jmsTemplate.convertAndSend(jmsQueue,jsonInString);
            return  new ResponseEntity<>(student.get(), HttpStatus.OK);

        }catch (JsonProcessingException e){
            e.printStackTrace();
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
