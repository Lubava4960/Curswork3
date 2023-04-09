package my.lubava.curswork3.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import my.lubava.curswork3.dto.SockDto;
import my.lubava.curswork3.exception.InsufficientSockQuantityException;
import my.lubava.curswork3.exception.InvalidSockRequestException;
import my.lubava.curswork3.model.Color;
import my.lubava.curswork3.model.Size;
import my.lubava.curswork3.model.Sock;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SockService {
    private final ObjectMapper objectMapper;
    private final AuditService auditService;
    private final Map<Sock, Integer> socks = new HashMap<>();

    public SockService(ObjectMapper objectMapper, AuditService auditService) {
          this.objectMapper = objectMapper;
          this.auditService = auditService;
    }

    public void addSock(SockDto sockDto){
        validateRequest(sockDto);
        Sock sock = mapToSock(sockDto);
        if (socks.containsKey(sock)){
            socks.put(sock, socks.get(sock) + sockDto.getQuantity());
        } else {
            socks.put(sock, sockDto.getQuantity());
        }
        auditService.recordAddOperation(sock, sockDto.getQuantity());
    }
    public void issueSock(SockDto sockDto){
        decreaseSockQuantity(sockDto, true);
    }


    public void removeDefectiveSocks(SockDto sockDto){
        decreaseSockQuantity(sockDto, false);
    }

    public FileSystemResource exportData() throws IOException {
        Path filePath = Files.createTempFile("export-", ".json");
       List<SockDto> sockList = new ArrayList<>();
        for (Map.Entry<Sock, Integer> entry : this.socks.entrySet()){
            sockList.add(mapToDto(entry.getKey(), entry.getValue()));
        }

        Files.write(filePath,objectMapper.writeValueAsBytes(sockList));
        return new FileSystemResource(filePath);
    }
    public void importData(InputStream inputStream) throws IOException {
        List<SockDto> importList = objectMapper.readValue(inputStream, new TypeReference<>() {
        });
        this.socks.clear();
        for (SockDto dto : importList){
            addSock(dto);
        }
    }
    private void decreaseSockQuantity(SockDto sockDto, boolean isIssue){
        validateRequest(sockDto);
        Sock sock = mapToSock(sockDto);
        int sockQuantity = socks.getOrDefault(sock, 0);
        if (sockQuantity >= sockDto.getQuantity()){
            socks.put(sock, sockQuantity - sockDto.getQuantity());
        } else {
            throw new InsufficientSockQuantityException("There is no socks");
        }
        if (isIssue){
            auditService.recordIssueOperation(sock, sockDto.getQuantity());
        } else {
            auditService.recordRemoveDefectedOperation(sock, sockDto.getQuantity());
        }
    }
    public int getSockQuantity(Color color, Size size, Integer cottonMin, Integer cottonMax){
        int total = 0;
        for (Map.Entry<Sock, Integer> entry : socks.entrySet()){
            if (color != null && !entry.getKey().getColor().equals(color)){
                continue;
            }
            if (size != null && !entry.getKey().getSize().equals(size)){
                continue;
            }
            if (cottonMin != null && entry.getKey().getCottonPercentage() < cottonMin){
                continue;
            }
            if (cottonMax != null && entry.getKey().getCottonPercentage() > cottonMax){
                continue;
            }
            total += entry.getValue();
        }
        return total;
    }

    public void validateRequest(SockDto sockDto){
        if (sockDto.getColor() == null || sockDto.getSize() == null){
            throw new InvalidSockRequestException("All fields should be filled");
        }
        if (sockDto.getCottonPercentage() < 0 || sockDto.getCottonPercentage() > 100){
            throw new InvalidSockRequestException("Cotton percentage should be between 0 and 100");
        }
        if (sockDto.getQuantity() <= 0) {
            throw new InvalidSockRequestException("Quantity should be more than 0");
        }
    }

    private Sock mapToSock(SockDto sockDto){
        return new Sock(sockDto.getColor(), sockDto.getSize(), sockDto.getCottonPercentage());
    }
    private SockDto mapToDto(Sock sock, int quantity){
        SockDto sockDto = new SockDto();
        sockDto.setColor(sock.getColor());
        sockDto.setSize(sock.getSize());
        sockDto.setCottonPercentage(sock.getCottonPercentage());
        sockDto.setQuantity(quantity);
        return sockDto;
    }
}







