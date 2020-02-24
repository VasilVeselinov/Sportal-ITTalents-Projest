package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.model.db.pojo.Role;
import sportal.model.db.repository.RoleRepository;
import sportal.model.service.IRoleService;
import sportal.model.service.dto.RoleServiceDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void seedRoleInDB() {
        this.roleRepository.saveAndFlush(new Role("EDITOR"));
        this.roleRepository.saveAndFlush(new Role("ADMIN"));
        this.roleRepository.saveAndFlush(new Role("USER"));
    }

    @Override
    public List<RoleServiceDTO> findAll() {
        List<Role> roleList = this.roleRepository.findAll();
        return RoleServiceDTO.fromPOJOToDTO(roleList);
    }
}