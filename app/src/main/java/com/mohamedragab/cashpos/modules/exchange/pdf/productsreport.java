package com.mohamedragab.cashpos.modules.exchange.pdf;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.settings.models.shopInfo;
import com.mohamedragab.cashpos.modules.store.models.product;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class productsreport extends AppCompatActivity {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;
    List<product> productsList;
    List<Double> quantityList;
    Context context;
    String number;
    Date c;

    public String setMoneyOmlareport(Context context, List<product> productsList, List<Double> quantityList) {
        this.productsList = productsList;
        this.quantityList = quantityList;
        this.context = context;
        c = Calendar.getInstance().getTime();
        SimpleDateFormat df2 = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.US);
        number = df2.format(c);
        createPdfWrapper();


        return number;
    }

    private void createPdfWrapper() {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            (dialog, which) -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {
            createPdf();


        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("تأكيد", okListener)
                .setNegativeButton("الغاء", null)
                .create()
                .show();
    }

    private void createPdf() {

        c = Calendar.getInstance().getTime();

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/cashpos");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
        }
     /*   SimpleDateFormat df2 = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.US);
        number = df2.format(c);*/

        String pdfname = number + ".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);

        OutputStream output = null;
        try {
            output = new FileOutputStream(pdfFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Font xxlarge = FontFactory.getFont("assets/droidkofi.ttf", BaseFont.IDENTITY_H, 32, Font.BOLD);
        Font largekofi = FontFactory.getFont("assets/droidkofi.ttf", BaseFont.IDENTITY_H, 13, Font.BOLD);
        Font large = FontFactory.getFont("assets/arial.ttf", BaseFont.IDENTITY_H, 18, Font.BOLD);
        Font medium = FontFactory.getFont("assets/arial.ttf", BaseFont.IDENTITY_H, 18, Font.NORMAL);
        Font mediumnormal = FontFactory.getFont("assets/arial.ttf", BaseFont.IDENTITY_H, 18, Font.BOLD);
        Font mediumnormal2 = FontFactory.getFont("assets/arial.ttf", BaseFont.IDENTITY_H, 20, Font.BOLD);
        Font small3 = FontFactory.getFont("assets/arial.ttf", BaseFont.IDENTITY_H, 18, Font.BOLD);
        Font small2 = FontFactory.getFont("assets/arial.ttf", BaseFont.IDENTITY_H, 14, Font.NORMAL);

        Rectangle pagesize = new Rectangle(645, 1200);
        Document document = new Document(pagesize);

        PdfPTable table = new PdfPTable(new float[]{3, 3, 3, 3, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        table.setTotalWidth(pagesize.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        Paragraph paragraph4 = getCellParagraph();
        Paragraph spacecell = getCellParagraph();
        spacecell.add("\n");
        spacecell.setAlignment(Element.ALIGN_CENTER);
        spacecell.setFont(large);
        PdfPCell spaceemptycell = getPdfPCellNoBorder(spacecell);

        Paragraph spacecellwithtopBorder = getCellParagraph();
        spacecellwithtopBorder.add("\n");
        spacecellwithtopBorder.setAlignment(Element.ALIGN_CENTER);
        spacecellwithtopBorder.setFont(large);
        PdfPCell spacecellwithtopBordercell = getPdfPCelltopBorder(spacecellwithtopBorder);

        table.addCell(spacecellwithtopBordercell);
        table.addCell(spacecellwithtopBordercell);
        table.addCell(spacecellwithtopBordercell);
        table.addCell(spacecellwithtopBordercell);
        table.addCell(spacecellwithtopBordercell);

        paragraph4.add("  العجز/ الزياده");
        paragraph4.setFont(largekofi);
        PdfPCell cell4 = getPdfPCellNoBorder(paragraph4);
        table.addCell(cell4);

        Paragraph paragraph5 = getCellParagraph();
        paragraph5.add("الكميه الحقيقيه");
        paragraph5.setFont(largekofi);
        PdfPCell cell5 = getPdfPCellNoBorder(paragraph5);
        table.addCell(cell5);

        Paragraph paragraph3 = getCellParagraph();

        paragraph3.add("   المخزن");
        paragraph3.setFont(largekofi);
        PdfPCell cell3 = getPdfPCellNoBorder(paragraph3);
        table.addCell(cell3);

        Paragraph paragraph2 = getCellParagraph();
        paragraph2.add("   اسم المنتج");
        paragraph2.setFont(largekofi);
        PdfPCell cell2 = getPdfPCellNoBorder(paragraph2);
        table.addCell(cell2);

        Paragraph paragraph = getCellParagraph();
        paragraph.add("     العدد");
        paragraph.setFont(largekofi);
        PdfPCell cell = getPdfPCellNoBorder(paragraph);
        table.addCell(cell);

        table.setHeaderRows(1);
        table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);


        table.addCell(spaceemptycell);
        table.addCell(spaceemptycell);
        table.addCell(spaceemptycell);
        table.addCell(spaceemptycell);
        table.addCell(spaceemptycell);

        for (int i = 0; i < productsList.size(); i++) {

            Paragraph num = getCellParagraph2();
            num.add(i + 1 + "");
            num.setAlignment(Element.ALIGN_CENTER);
            num.setFont(medium);
            PdfPCell productnamecell4 = getPdfPCelltopBorder(num);
            table.addCell(productnamecell4);

            Paragraph name = getCellParagraph2();
            name.add(productsList.get(i).getName() + "");
            name.setAlignment(Element.ALIGN_CENTER);
            name.setFont(medium);
            PdfPCell productnamecell2 = getPdfPCelltopBorder(name);
            table.addCell(productnamecell2);

            Paragraph quan1 = getCellParagraph2();
            quan1.add(quantityList.get(i) + "");
            quan1.setAlignment(Element.ALIGN_CENTER);
            quan1.setFont(medium);
            PdfPCell productnamecell = getPdfPCelltopBorder(quan1);
            table.addCell(productnamecell);

            Paragraph quan2 = getCellParagraph2();
            quan2.add(productsList.get(i).getQuantity() + "");
            quan2.setAlignment(Element.ALIGN_CENTER);
            quan2.setFont(medium);
            PdfPCell productQuantitycell = getPdfPCelltopBorder(quan2);
            table.addCell(productQuantitycell);

            Paragraph minus = getCellParagraph2();
            minus.add((productsList.get(i).getQuantity()-quantityList.get(i)) + "");
            minus.setAlignment(Element.ALIGN_CENTER);
            minus.setFont(small2);
            PdfPCell productsellpricecell = getPdfPCelltopBorder(minus);
            table.addCell(productsellpricecell);


            table.addCell(spaceemptycell);
            table.addCell(spaceemptycell);
            table.addCell(spaceemptycell);
            table.addCell(spaceemptycell);
            table.addCell(spaceemptycell);

        }
        table.addCell(spacecellwithtopBordercell);
        table.addCell(spacecellwithtopBordercell);
        table.addCell(spacecellwithtopBordercell);
        table.addCell(spacecellwithtopBordercell);
        table.addCell(spacecellwithtopBordercell);

        try {
            PdfWriter.getInstance(document, output);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.open();

        shopInfo info = SheredPrefranseHelper.getshopData(context);

        Paragraph shopname = getCellParagraph2();
        if (info != null) {
            if (!info.getNotification().equals("")) {
                shopname.add(info.getShopName() + "");
            } else {
                shopname.add("CashPOS");
            }
        } else {
            shopname.add("CashPOS");
        }
        shopname.setAlignment(Element.ALIGN_CENTER);
        shopname.setFont(xxlarge);
        PdfPCell shopnamecell = getPdfPCellNoBorder(shopname);

        Paragraph title = getCellParagraph();
        title.add("تقرير جرد المنتجات");
        title.setAlignment(Element.ALIGN_CENTER);
        title.setFont(xxlarge);
        PdfPCell cell1 = getPdfPCellNoBorder(title);

        Paragraph cashername = getCellParagraph();
        if (SheredPrefranseHelper.getcurrentcashier(context) != null) {
            if (!SheredPrefranseHelper.getcurrentcashier(context).getName().equals("")) {
                cashername.add(" اسم الموظف : " + SheredPrefranseHelper.getcurrentcashier(context).getName() + "");
            } else {
                cashername.add(" اسم الموظف : " + "لا يوجد");
            }
        } else {
            cashername.add(" اسم الموظف : " + "لا يوجد");
        }
        cashername.setAlignment(Element.ALIGN_LEFT);
        cashername.setFont(medium);
        PdfPCell cashernamecell = getPdfPCellNoBorder(cashername);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        String currentdate = df.format(c);

        Paragraph creationDate = getCellParagraph();
        creationDate.add("التاريخ  :  " + currentdate);
        creationDate.setAlignment(Element.ALIGN_LEFT);
        creationDate.setFont(mediumnormal);
        PdfPCell creationDatecell = getPdfPCellNoBorder(creationDate);


        Paragraph thanks = getCellParagraph2();
        thanks.add("CashPOS لخدمات البيع الذكيه ");
        thanks.setAlignment(Element.ALIGN_CENTER);
        thanks.setFont(small3);
        PdfPCell thankscell = getPdfPCellNoBorder(thanks);


        PdfPTable headerTable = new PdfPTable(1);
        PdfPTable downtable = new PdfPTable(1);

        downtable.addCell(spaceemptycell);
        downtable.addCell(thankscell);
        downtable.addCell(spaceemptycell);

        headerTable.addCell(creationDatecell);
        headerTable.addCell(spaceemptycell);

        headerTable.addCell(shopnamecell);
        headerTable.addCell(spaceemptycell);
        headerTable.addCell(cell1);
        headerTable.addCell(spaceemptycell);

        headerTable.addCell(cashernamecell);
        headerTable.addCell(spaceemptycell);

        try {
            document.add(headerTable);
            document.add(table);
            document.add(downtable);
            document.close();


        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }


    private Paragraph getCellParagraph() {
        Paragraph paragraph = new Paragraph(20f);
        paragraph.setAlignment(Paragraph.ALIGN_JUSTIFIED);

        return paragraph;
    }

    private Paragraph getCellParagraph2() {
        Paragraph paragraph = new Paragraph(20f);
        paragraph.setAlignment(Paragraph.ALIGN_JUSTIFIED);

        return paragraph;
    }

    private PdfPCell getPdfPCellNoBorder(Paragraph paragraph) {
        PdfPCell cell = new PdfPCell();
        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        cell.setPaddingBottom(5);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.addElement(paragraph);
        return cell;
    }

    private PdfPCell getPdfPCelltopBorder(Paragraph paragraph) {
        PdfPCell cell = new PdfPCell();
        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        cell.setPaddingBottom(5);
        cell.setPaddingTop(5);
        cell.setBorder(Rectangle.TOP);
        cell.addElement(paragraph);
        return cell;
    }


}