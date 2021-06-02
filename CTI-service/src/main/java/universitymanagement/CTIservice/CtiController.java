package universitymanagement.CTIservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cti")
public class CtiController {
    @Autowired
    StudentRepository studentRepository;

    @GetMapping("/getStudents")
    List<Student> getCtiStudent() {
        return studentRepository.findAll();
    }

    @GetMapping("/deleteOne/{id}")
    void deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
    }

    @GetMapping("/deleteAll")
    void deleteStudents() {
        studentRepository.deleteAll();
    }

    @GetMapping("/info")
    String getInfo() {
        return "cti microservice";
    }
}
