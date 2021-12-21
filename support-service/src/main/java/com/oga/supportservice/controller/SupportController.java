package com.oga.supportservice.controller;


import com.oga.supportservice.dto.SupportDto;
import com.oga.supportservice.entity.SupportEntity;
import com.oga.supportservice.services.ImageService;
import com.oga.supportservice.services.SupportService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("api")
public class SupportController {
    private SupportService supportService;
    private ImageService imageService;


    @Autowired
    public SupportController(SupportService supportService, ImageService imageService) {
        this.supportService = supportService;
        this.imageService = imageService;
    }

    @PostMapping("/user/{id}/ticket2")
    public SupportEntity addTicket2(@PathVariable(name = "id") long id,@RequestBody SupportEntity request){
        return supportService.addTicket(id,request).getBody();
    }

    @RequestMapping(value = "/user/{id}/ticket", method = RequestMethod.POST, consumes = { "multipart/form-data" })
    public ResponseEntity<SupportEntity> addTicket(@ModelAttribute SupportEntity request, @PathVariable(name = "id") long id, MultipartFile file){

//        if (userService.getUserByUserName(user.getUserName())!=null){
//            return new ResponseEntity(new UserCreationDto(user,"null","this "),HttpStatus.BAD_REQUEST);
//        }
        String fileName = imageService.storeFile(file);


       String extension = FilenameUtils.getExtension(fileName);
        request.setFileName(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/downloadFile/")
                .path((fileName+new SimpleDateFormat("dd-MM-yyyy").format(new Date())+id+new SimpleDateFormat("hh:mm:ss").format(new Date()))+extension)

                .toUriString());

        SupportEntity ticket = new SupportEntity();

        return new ResponseEntity(supportService.addTicket(id,request).getBody(), HttpStatus.CREATED);
    }
    @GetMapping("tickets")
    public List<SupportEntity> getAllSupport(){
        return supportService.getAllTickets();
    }

    @GetMapping("tickets/{id}")
    public List<SupportEntity> getAllSupport(@PathVariable(name = "id")long id){
        return supportService.getAllTicketByIdUser(id);
    }



    @GetMapping("ticket/{id}")
    public SupportEntity getTicketById(@PathVariable(name = "id")long id){
        return supportService.getTicketById(id).get();
    }

    @PutMapping("ticket/{id}")
    public SupportEntity updateTicket(@PathVariable(name = "id")long id,@RequestBody SupportEntity supportEntity){
        return supportService.updateTicket(id,supportEntity);
    }

    @GetMapping("downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        // Load file as Resource
        Resource resource = imageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;

        contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
