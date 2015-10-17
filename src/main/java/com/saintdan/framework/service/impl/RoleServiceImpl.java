package com.saintdan.framework.service.impl;

import com.saintdan.framework.component.ResultHelper;
import com.saintdan.framework.constant.ControllerConstant;
import com.saintdan.framework.enums.ErrorType;
import com.saintdan.framework.exception.RoleException;
import com.saintdan.framework.param.RoleParam;
import com.saintdan.framework.po.Role;
import com.saintdan.framework.repo.RoleRepository;
import com.saintdan.framework.service.RoleService;
import com.saintdan.framework.vo.RoleVO;
import com.saintdan.framework.vo.RolesVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the
 * {@link RoleService}
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 10/17/15
 * @since JDK1.8
 */
public class RoleServiceImpl implements RoleService {

    // ------------------------
    // PUBLIC METHODS
    // ------------------------

    /**
     * Create new role.
     *
     * @param param         role params
     * @return              role VO
     * @throws RoleException        ROL0031 Role already existing, name taken.
     */
    @Override
    public RoleVO create(RoleParam param) throws RoleException {
        Role role = roleRepository.findByName(param.getName());
        if (role != null) {
            // Throw role already existing, name taken.
            throw new RoleException(ErrorType.ROL0031);
        }
        return rolePO2VO(roleRepository.save(roleParam2PO(param)),
                String.format(ControllerConstant.CREATE, ROLE));
    }

    /**
     * Show all roles VO.
     *
     * @return              roles VO
     * @throws RoleException        ROL0011 No role yet.
     */
    @Override
    public RolesVO getAllRoles() throws RoleException {
        List<Role> roles = (List<Role>) roleRepository.findAll();
        if (roles == null) {
            // Throw user cannot find by usr parameter exception.
            throw new RoleException(ErrorType.ROL0011);
        }
        return rolesPO2VO(roles, String.format(ControllerConstant.INDEX, ROLE));
    }

    /**
     * Show role VO by role id.
     *
     * @param param         role params
     * @return              role VO
     * @throws RoleException
     */
    @Override
    public RoleVO getRoleById(RoleParam param) throws RoleException {
        Role role = roleRepository.findOne(param.getId());
        if (role == null) {
            // Throw role cannot find by id parameter exception.
            throw new RoleException(ErrorType.ROL0012);
        }
        return rolePO2VO(role, String.format(ControllerConstant.SHOW, ROLE));
    }

    @Override
    public RoleVO update(RoleParam param) throws RoleException {
        return null;
    }

    @Override
    public void delete(RoleParam param) throws RoleException {

    }

    // ------------------------
    // PRIVATE FIELDS AND METHODS
    // ------------------------

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ResultHelper resultHelper;

    private final static String ROLE = "role";

    /**
     * Transform role param to PO.
     *
     * @param param         role param
     * @return              role PO
     */
    private Role roleParam2PO(RoleParam param) {
        Role role = new Role();
        BeanUtils.copyProperties(param, role);
        return role;
    }

    /**
     * Transform role PO to VO.
     *
     * @param role          role PO
     * @param msg           return message
     * @return              role VO
     */
    private RoleVO rolePO2VO(Role role, String msg) {
        RoleVO vo = new RoleVO();
        BeanUtils.copyProperties(role, vo);
        if (StringUtils.isBlank(msg)) {
            return vo;
        }
        vo.setMessage(msg);
        // Return success result.
        return (RoleVO) resultHelper.sucessResp(vo);
    }

    /**
     * Transform roles PO to roles VO
     *
     * @param roles     roles PO
     * @param msg       return message
     * @return          roles VO
     */
    private RolesVO rolesPO2VO(Iterable<Role> roles, String msg) {
        RolesVO vos = new RolesVO();
        List<RoleVO> roleVOList = new ArrayList<>();
        for (Role role : roles) {
            RoleVO vo = rolePO2VO(role, "");
            roleVOList.add(vo);
        }
        vos.setRoleVOList(roleVOList);
        if (StringUtils.isBlank(msg)) {
            return vos;
        }
        vos.setMessage(msg);
        return (RolesVO) resultHelper.sucessResp(vos);
    }
}
