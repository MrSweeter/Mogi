package be.msdc.mogi.settings;

import be.msdc.mogi.models.MogiResult;
import be.msdc.mogi.models.ProcessType;
import be.msdc.mogi.models.commands.MogiCommand;
import be.msdc.mogi.models.commands.WhereWhichCommand;
import be.msdc.mogi.utils.MogiException;
import be.msdc.mogi.utils.ProcessRunner;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

public class MogiSettingsPanel {

    private JComponent rootPanel;
    private JTabbedPane tabPanel;

    private TextFieldWithBrowseButton whereWhichPath;
    private JLabel whereWhichErrorMessage;
    private JCheckBox useInit;
    private JCheckBox useForce;
    private JCheckBox useCheckout;
    private JCheckBox useRecursive;
    private JCheckBox useSync;
    private JTextField checkoutGitBranch;
    private TextFieldWithBrowseButton gitPath;
    private JLabel gitErrorMessage;
    private JTextField customCommand;
    private JCheckBox useIntellijGitPull;
    private JCheckBox checkoutAskEachTime;

    //region Public
    public JComponent getPanel() {
        return rootPanel;
    }

    public void load(MogiSettings settings) {
        whereWhichLoad(settings);
        gitLoad(settings);
        customLoad(settings);
    }

    public boolean isModified(MogiSettings settings) {
        return whereWhichModified(settings)
                || gitModified(settings)
                || customModified(settings);
    }

    public void save(MogiSettings settings) {
        whereWhichSave(settings);
        gitSave(settings);
        customSave(settings);
    }
    //endregion

    //region WhereWhich
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

    //region Git
    private void gitLoad(MogiSettings settings) {
        gitErrorMessage.setText("");
        useInit.setSelected(settings.getUseInit());
        useForce.setSelected(settings.getUseForce());
        useCheckout.setSelected(settings.getUseCheckout());
        useRecursive.setSelected(settings.getUseRecursive());
        useSync.setSelected(settings.getUseSync());
        useIntellijGitPull.setSelected(settings.getUseIntellijPull());
        checkoutGitBranch.setText(settings.getCheckoutGitBranch());
        checkoutAskEachTime.setSelected(settings.getCheckoutGitBranchRequest());
        gitPath.setText(settings.getGitPath().trim());
    }

    private boolean gitModified(MogiSettings settings) {
        return settings.getUseInit() != useInit.isSelected()
                || settings.getUseCheckout() != useCheckout.isSelected()
                || settings.getUseForce() != useForce.isSelected()
                || settings.getUseRecursive() != useRecursive.isSelected()
                || settings.getUseSync() != useSync.isSelected()
                || settings.getUseIntellijPull() != useIntellijGitPull.isSelected()
                || !settings.getCheckoutGitBranch().equals(checkoutGitBranch.getText())
                || settings.getCheckoutGitBranchRequest() != checkoutAskEachTime.isSelected()
                || !settings.getGitPath().equals(gitPath.getText());
    }

    private void gitSave(MogiSettings settings) {
        settings.setUseInit(useInit.isSelected());
        settings.setUseForce(useForce.isSelected());
        settings.setUseCheckout(useCheckout.isSelected());
        settings.setUseRecursive(useRecursive.isSelected());
        settings.setUseSync(useSync.isSelected());
        settings.setUseIntellijPull(useIntellijGitPull.isSelected());
        settings.setCheckoutGitBranch(checkoutGitBranch.getText());
        settings.setCheckoutGitBranchRequest(checkoutAskEachTime.isSelected());
        settings.setGitPath(gitPath.getText());
    }
    //endregion

    //region Custom
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

    /*private Project getProject() {
        DataManager dm = DataManager.getInstanceIfCreated();
        if (dm != null) {
            return (Project) dm.getDataContext(rootPanel).getData(CommonDataKeys.PROJECT.getName());
        }
        return null;
    }*/
    //endregion
}