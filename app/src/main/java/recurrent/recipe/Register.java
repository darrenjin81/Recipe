package recurrent.recipe;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class Register extends Fragment {
    private EditText etEmail;
    private EditText etPassword;
    private Button btnCreate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        etEmail = (EditText) view.findViewById(R.id.etRegisterEmailField);
        etPassword = (EditText) view.findViewById(R.id.etRegisterPasswordField);
        btnCreate = (Button) view.findViewById(R.id.btnCreate);
    }
}
