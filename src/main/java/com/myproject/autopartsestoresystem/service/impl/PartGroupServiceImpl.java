package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.PartGroupDTO;
import com.myproject.autopartsestoresystem.exception.service.PartGroupAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.service.PartGroupNotFoundException;
import com.myproject.autopartsestoresystem.mapper.PartGroupMapper;
import com.myproject.autopartsestoresystem.model.PartGroup;
import com.myproject.autopartsestoresystem.model.PartGroupType;
import com.myproject.autopartsestoresystem.repository.PartGroupRepository;
import com.myproject.autopartsestoresystem.service.PartGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class PartGroupServiceImpl implements PartGroupService {

    private final PartGroupRepository partGroupRepository;
    private final PartGroupMapper partGroupMapper;

    @Override
    public PartGroupDTO save(PartGroupDTO partGroupDTO) {

        if(partGroupRepository.findByName(partGroupDTO.getName()).isPresent())
            throw new PartGroupAlreadyExistsException("Part group already exists!");

        PartGroup saved = partGroupRepository.save(partGroupMapper.partGroupDTOToPartGroup(partGroupDTO));

        return partGroupMapper.partGroupToPartGroupDTO(saved);
    }

    @Override
    public PartGroupDTO update(Long id, PartGroupDTO partGroupDTO) {

        PartGroup found = partGroupRepository.findById(id).orElseThrow(() -> new PartGroupNotFoundException("Part group not found"));

        found.setName(partGroupDTO.getName());
        found.setParts(partGroupDTO.getParts());

        PartGroup updated = partGroupRepository.save(found);

        return partGroupMapper.partGroupToPartGroupDTO(updated);
    }

    @Override
    public List<PartGroupDTO> getAll() {
        return partGroupRepository.findAll().stream().map(partGroupMapper::partGroupToPartGroupDTO).collect(Collectors.toList());
    }

    @Override
    public PartGroupDTO getById(Long id) {
        PartGroup partGroup = partGroupRepository.findById(id).orElseThrow(() -> new PartGroupNotFoundException("Part group not found"));
        return partGroupMapper.partGroupToPartGroupDTO(partGroup);
    }

    @Override
    public void delete(Long id) {

        if(!partGroupRepository.existsById(id))
            throw new PartGroupNotFoundException("Part group not found");

        partGroupRepository.deleteById(id);
    }
}
