package com.example.scheduletracker.service.impl;

import com.example.scheduletracker.dto.LessonCountDto;
import com.example.scheduletracker.repository.LessonRepository;
import com.example.scheduletracker.service.AnalyticsService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {
    private final LessonRepository lessonRepository;

    public AnalyticsServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public List<LessonCountDto> lessonCountPerTeacher() {
        return lessonRepository.countLessonsPerTeacher();
    }

    @Override
    public byte[] exportLessonCountExcel() {
        List<LessonCountDto> counts = lessonCountPerTeacher();
        try (Workbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            var sheet = wb.createSheet("Lessons");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Teacher");
            header.createCell(1).setCellValue("Lessons");
            int i = 1;
            for (LessonCountDto dto : counts) {
                Row row = sheet.createRow(i++);
                row.createCell(0).setCellValue(dto.teacherName());
                row.createCell(1).setCellValue(dto.lessonCount());
            }
            wb.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel", e);
        }
    }
}
