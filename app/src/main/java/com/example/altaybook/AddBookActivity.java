package com.example.altaybook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.ExecutionException;

public class AddBookActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE =
            "com.example.altaybook.EXTRA_TITLE";
    public static final String EXTRA_PDF_TEXT =
            "com.example.altaybook.EXTRA_PDF_TEXT";
    public static final String EXTRA_BOOK_IMAGE =
            "com.example.altaybook.EXTRA_BOOK_IMAGE";
    public static final int FILE_SELECT_CODE = 0;

    private EditText editTextTitle;
    private EditText editTextPdfText;
    private Button buttonFileSelect;
    private String pdfText = "";
    private ImageView pdfImageView;
    BookViewModel bookViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextPdfText = findViewById(R.id.edit_text_pdf_text);
        buttonFileSelect = findViewById(R.id.button_file_select);
        pdfImageView = findViewById(R.id.pdfImageView);

        bookViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(BookViewModel.class);

        buttonFileSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFiles();
            }
        });
    }

    private void openFiles() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "İndirilecek dosyayı seçin."), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Lütfen bir dosya yöneticisi indirin.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String path = FileUtils.getRealPath(this, uri);
            editTextPdfText.setText(path);
        }
    }

    private void saveBook() {
        final String title = editTextTitle.getText().toString();
        String pdfPath = editTextPdfText.getText().toString();

        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Lütfen bir kitap adı giriniz.", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(pdfPath);
        if(!file.exists()){
            Toast.makeText(this, "Dosya bulunamadı.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!pdfPath.substring(pdfPath.length() - 3).equals("pdf")) {
            Toast.makeText(this, "Lütfen bir PDF dosyası giriniz.", Toast.LENGTH_SHORT).show();
            return;
        }

            new PdfTextAsyncTask().execute(pdfPath, pdfText);
        new PdfImageAsyncTask(this, new OnPdfImageAsyncTaskFinished() {
            @Override
            public void onFinished(String pdfText, byte[] bytePdfImage) {
                Intent data = new Intent(AddBookActivity.this, MainActivity.class);
                Book book = new Book(bytePdfImage, title, pdfText);
                bookViewModel.insert(book);

                setResult(RESULT_OK, data);
                finish();
            }
        }).execute(pdfPath);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_book:
                saveBook();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class PdfTextAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                PdfReader reader = new PdfReader(strings[0]);
                int n = reader.getNumberOfPages();
                for (int i = 0; i < n; i++) {
                    strings[1] = strings[1] + PdfTextExtractor.getTextFromPage(reader, i + 1).trim() + "\n";
                }
                reader.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            return strings[1];
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pdfText = result;
        }
    }

    private class PdfImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private AddBookActivity addBookActivity;
        private OnPdfImageAsyncTaskFinished onPdfImageAsyncTaskFinished;
        LoadingDiyalog loadingDiyalog;
        int width = 110;
        int height = 141;

        private PdfImageAsyncTask(AddBookActivity addBookActivity, OnPdfImageAsyncTaskFinished onPdfImageAsyncTaskFinished) {
            this.onPdfImageAsyncTaskFinished = onPdfImageAsyncTaskFinished;
            this.addBookActivity = addBookActivity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDiyalog = new LoadingDiyalog(addBookActivity);
            loadingDiyalog.startLoadingDialog();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;

            try {
                File file = new File(strings[0]);
                PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));

                PdfRenderer.Page page = renderer.openPage(0);

                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                page.close();

                renderer.close();
            } catch (Exception ex) {
                Log.e("HATA", "Bitmap boş");
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            byte[] bytePdfImage = bitmapToBytes(bitmap);
            loadingDiyalog.dismissDialog();
            onPdfImageAsyncTaskFinished.onFinished(pdfText, bytePdfImage);
        }
    }

    public byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

}
