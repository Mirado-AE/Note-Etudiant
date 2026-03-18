package com.note;

import com.note.model.Note;
import com.note.service.NoteServ;
import com.note.service.NoteFinalServ;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(NoteServ noteServ, NoteFinalServ noteFinalServ) {
        return args -> {

            Long candidatId = 1L;
            Long matiereId = 1L;
            Long examenId = 1L;

            List<Note> notes = noteServ.getNotesByCandidatIdAndMatiereIdAndExamenId(
                    candidatId, matiereId, examenId
            );

            System.out.println("Notes du candidat " + candidatId + " pour la matière " + matiereId + " et examen " + examenId + ":");
            for (Note n : notes) {
                System.out.println(n.getNote());
            }

            Double sumDiffs = noteServ.getSumDiffsNotes(notes);

            System.out.println("Somme des différences entre notes : " + sumDiffs);

            double noteFinal = noteFinalServ.getNoteFinalByMatiere(candidatId, matiereId, examenId);

            System.out.println("Note final = " + noteFinal);

        };
    }


}