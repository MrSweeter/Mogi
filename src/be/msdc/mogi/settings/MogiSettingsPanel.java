package be.msdc.mogi.settings;

import be.msdc.mogi.models.MogiResult;
import be.msdc.mogi.models.ProcessType;
import be.msdc.mogi.models.commands.MogiCommand;
import be.msdc.mogi.models.commands.WhereWhichCommand;
import be.msdc.mogi.utils.MogiException;
import be.msdc.mogi.utils.ProcessRunner;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

public class MogiSettingsPanel {

    private JComponent rootPanel;
    private JTabbedPane tabPanel;

    //region Public

    public JComponent getPanel() {
        return rootPanel;
    }

    //region WhereWhich
    private TextFieldWithBrowseButton whereWhichPath;
    private JLabel whereWhichErrorMessage;
    //region Git
    private JCheckBox useInit;
    //endregion
    private JCheckBox useForce;
    private JCheckBox useCheckout;
    private JCheckBox useRecursive;
    private JCheckBox useSync;
    private JTextField checkoutGitBranch;
    //endregion
    private TextFieldWithBrowseButton gitPath;
    private JLabel gitErrorMessage;
    //region Gradlew
    private TextFieldWithBrowseButton gradlewPath;
    private JLabel gradlewErrorMessage;
    //region Custom
    private JTextField customCommand;

    public void load(MogiSettings settings) {
        whereWhichLoad(settings);
        gitLoad(settings);
        gradlewLoad(settings);
        customLoad(settings);
    }

    public boolean isModified(MogiSettings settings) {
        return whereWhichModified(settings)
                || gitModified(settings)
                || gradlewModified(settings)
                || customModified(settings);
    }

    public void save(MogiSettings settings) {
        whereWhichSave(settings);
        gitSave(settings);
        gradlewSave(settings);
        customSave(settings);
    }

    private void whereWhichLoad(MogiSettings settings) {
        whereWhichErrorMessage.setText("");
        whereWhichPath.setText(settings.getWhereWhichPath().trim());
    }

    private boolean whereWhichModified(MogiSettings settings) {
        return !settings.getWhereWhichPath().equals(whereWhichPath.getText());
    }

    private void whereWhichSave(MogiSettings settings) {
        settings.setWhereWhichPath(whereWhichPath.getText());
    }
    //endregion

    private void gitLoad(MogiSettings settings) {
        gitErrorMessage.setText("");
        useInit.setSelected(settings.getUseInit());
        useForce.setSelected(settings.getUseForce());
        useCheckout.setSelected(settings.getUseCheckout());
        useRecursive.setSelected(settings.getUseRecursive());
        checkoutGitBranch.setText(settings.getCheckoutGitBranch());
        gitPath.setText(settings.getGitPath().trim());
    }

    private boolean gitModified(MogiSettings settings) {
        return settings.getUseInit() != useInit.isSelected()
                || settings.getUseCheckout() != useCheckout.isSelected()
                || settings.getUseForce() != useForce.isSelected()
                || settings.getUseRecursive() != useRecursive.isSelected()
                || settings.getUseSync() != useSync.isSelected()
                || !settings.getCheckoutGitBranch().equals(checkoutGitBranch.getText())
                || !settings.getGitPath().equals(gitPath.getText());
    }

    private void gitSave(MogiSettings settings) {
        settings.setUseInit(useInit.isSelected());
        settings.setUseForce(useForce.isSelected());
        settings.setUseCheckout(useCheckout.isSelected());
        settings.setUseRecursive(useRecursive.isSelected());
        settings.setUseSync(useSync.isSelected());
        settings.setCheckoutGitBranch(checkoutGitBranch.getText());
        settings.setGitPath(gitPath.getText());
    }

    private void gradlewLoad(MogiSettings settings) {
        gradlewErrorMessage.setText("");
        gradlewPath.setText(settings.getWhereWhichPath().trim());
    }

    private boolean gradlewModified(MogiSettings settings) {
        return !settings.getGradlewPath().equals(gradlewPath.getText());
    }
    //endregion

    private void gradlewSave(MogiSettings settings) {
        settings.setGradlewPath(gradlewPath.getText());
    }

    private void customLoad(MogiSettings settings) {
        customCommand.setText(settings.getUserCustomCommand());
    }

    private boolean customModified(MogiSettings settings) {
        return !settings.getUserCustomCommand().equals(customCommand.getText());
    }

    private void customSave(MogiSettings settings) {
        settings.setUserCustomCommand(customCommand.getText());
    }
    //endregion

    //region private
    private void createUIComponents() {
        gitPath = new TextFieldWithBrowseButton();
        final FileChooserDescriptor desc = FileChooserDescriptorFactory.createSingleLocalFileDescriptor();
        gitPath.addBrowseFolderListener("Mogi Git Path", "Choose git path", null, desc);
        gitPath.getTextField().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                verifyPath(ProcessType.GIT, gitPath.getTextField(), gitErrorMessage, false);
            }
        });

        gradlewPath = new TextFieldWithBrowseButton();
        final FileChooserDescriptor gdesc = FileChooserDescriptorFactory.createSingleLocalFileDescriptor();
        gradlewPath.addBrowseFolderListener("Mogi Git Path", "Choose git path", null, gdesc);
        gradlewPath.getTextField().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                verifyPath(ProcessType.GRADLEW, gradlewPath.getTextField(), gradlewErrorMessage, false);
            }
        });

        whereWhichPath = new TextFieldWithBrowseButton();
        final FileChooserDescriptor wDesc = FileChooserDescriptorFactory.createSingleLocalFileDescriptor();
        whereWhichPath.addBrowseFolderListener("Mogi Where/Which Path", "Choose where/which path", null, wDesc);
        whereWhichPath.getTextField().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (verifyPath(ProcessType.WHERE_WHICH, whereWhichPath.getTextField(), whereWhichErrorMessage, !gitPath.getText().isEmpty())) {
                    updateGitFromWhereWhich();
                    updateGradlewFromWhereWhich();
                }
            }
        });
    }

    private boolean verifyPath(ProcessType type, JTextField field, JLabel error, boolean allowEmpty) {
        String text = field.getText();
        if (text == null || text.isEmpty()) {
            if (allowEmpty) return true;
            error.setText(text + " must be fulfilled");
            return false;
        }
        if (!new File(text).isFile()) {
            error.setText(text + " must be a file");
            return false;
        }
        if (!type.isValid(text)) {
            error.setText(text + " end is not equals to " + type.getExecutableName());
            return false;
        }
        error.setText("");
        return true;
    }

    private void updateGitFromWhereWhich() {
        if (gitPath.getText().isEmpty() && !whereWhichPath.getText().isEmpty()) {

            try {
                MogiCommand cmd = new WhereWhichCommand((ProcessType.GIT.getExecutableName()));
                cmd.setForceExecutable(whereWhichPath.getText());

                MogiResult out = ProcessRunner.INSTANCE.run(cmd, null);
                if (gitPath.getText().isEmpty() && out.isSuccess()) {
                    gitPath.setText(out.getSuccess().trim());
                } else if (!out.isSuccess()) {
                    whereWhichErrorMessage.setText(out.getFail());
                }
            } catch (MogiException ex) {
                whereWhichErrorMessage.setText(ex.getLocalizedMessage());
            }
        }
    }

    private void updateGradlewFromWhereWhich() {
        if (gradlewPath.getText().isEmpty() && !whereWhichPath.getText().isEmpty()) {

            try {
                MogiCommand cmd = new WhereWhichCommand((ProcessType.GRADLEW.getExecutableName()));
                cmd.setForceExecutable(whereWhichPath.getText());

                DataManager dm = DataManager.getInstanceIfCreated();
                if (dm != null) {
                    Project project = (Project) dm.getDataContext(rootPanel).getData(CommonDataKeys.PROJECT.getName());

                    if (project != null) {
                        MogiResult out = ProcessRunner.INSTANCE.run(cmd, project.getBasePath());
                        if (gitPath.getText().isEmpty() && out.isSuccess()) {
                            gitPath.setText(out.getSuccess().trim());
                        } else if (!out.isSuccess()) {
                            whereWhichErrorMessage.setText(out.getFail());
                        }
                    }
                }
            } catch (MogiException ex) {
                whereWhichErrorMessage.setText(ex.getLocalizedMessage());
            }
        }
    }
    //endregion
}