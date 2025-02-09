package org.proha.utils;
import jakarta.enterprise.context.ApplicationScoped;
import org.proha.model.dto.AddressDTO;
import org.proha.model.dto.StudentDTO;
import org.proha.model.entity.Address;
import org.proha.model.entity.Subject;
import org.proha.model.entity.Student;

@ApplicationScoped
public class StudentMapper {

    public StudentDTO toDTO(Student student) {
        AddressDTO addressDTO = mapAddressToDTO(student.getAddress());
        return new StudentDTO(student.getId(), student.getName(), student.getEmail(), student.getPhone(), addressDTO);
    }

    public Student toEntity(StudentDTO studentDTO) {
        Student student = new Student();
        student.setId(studentDTO.getId());
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setPhone(studentDTO.getPhone());
        student.setAddress(mapAddressToEntity(studentDTO.getAddress()));
        return student;
    }

    private AddressDTO mapAddressToDTO(Address address) {
        if (address == null) return null;
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        return addressDTO;
    }

    public Address mapAddressToEntity(AddressDTO addressDTO) {
        if (addressDTO == null) return null;
        Address address = new Address();
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        return address;
    }
}
