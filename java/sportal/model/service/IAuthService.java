package sportal.model.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import sportal.exception.BadRequestException;
import sportal.model.service.dto.RoleServiceDTO;
import sportal.model.service.dto.UserServiceDTO;

import java.sql.SQLException;
import java.util.List;

import static sportal.util.GlobalConstants.HAS_AUTHORITY_EDITOR;

public interface IAuthService extends UserDetailsService {

    void registration(UserServiceDTO serviceDTO) throws SQLException;

    UserServiceDTO changePassword(UserServiceDTO serviceDTO, long userId);

    UserServiceDTO findUserByUsername(String username);

    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    void authorise(long userId, List<RoleServiceDTO> editorAuthorities) throws BadRequestException;
}
