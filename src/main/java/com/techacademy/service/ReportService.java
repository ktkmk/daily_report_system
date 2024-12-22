package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;

import jakarta.transaction.Transactional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    // 日報保存
    @Transactional
    public ErrorKinds save(Report report, Employee employee) {
        // 同一日付のデータ件数取得
        Integer count = reportRepository.countByEmployeeAndReportDate(employee, report.getReportDate());

        // 同一日付チェック
        if (count > 0) {
            return ErrorKinds.DATECHECK_ERROR;
        }

        report.setDeleteFlg(false);

        report.setEmployee(employee);

        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setUpdatedAt(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

    // 日報更新
    @Transactional
    public ErrorKinds update(Report report, Employee employee) {

        // 同一日付のデータの件数取得（同一レポートIDは更新対象）
        List<Report> existReportList = reportRepository.findByEmployeeAndReportDateAndIdNot(employee, report.getReportDate(), report.getId());
        Report beforeReport = findById(report.getId());

        // 同一日付チェック
        if (existReportList.size() > 0) {
            return ErrorKinds.DATECHECK_ERROR;
        }

        // 登録日時は更新前データにて設定
        report.setCreatedAt(beforeReport.getCreatedAt());

        report.setEmployee(beforeReport.getEmployee());

        // 更新日時
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

    // 日報削除
    @Transactional
    public ErrorKinds delete(Integer id) {

        Report report = findById(id);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setDeleteFlg(true);

        return ErrorKinds.SUCCESS;
    }

    // 日報一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    // 1件を検索
    public Report findById(Integer id) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }

    // 日報一覧表示処理
    public List<Report> findByEmployee(Employee employee) {
        return reportRepository.findByEmployee(employee);
    }




}
