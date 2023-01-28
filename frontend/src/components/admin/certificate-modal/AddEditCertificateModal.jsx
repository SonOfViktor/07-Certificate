import Dialog from '@mui/material/Dialog';
import FormHat from '../../ui/FormHat';
import {useMediaQuery} from '@mui/material';
import {useTheme} from '@emotion/react';
import CertificateForm from './CertificateForm';
import {backgroundProperty} from './const/modalProperty';

const AddEditCertificateModal = ({open, handleClose, certificate}) => {
  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down('sm'));
  const title = certificate ? 'Edit Certificate' : 'Add New Certificate';

  return (
    <Dialog
      open={open}
      onClose={handleClose}
      fullWidth
      maxWidth="md"
      fullScreen={fullScreen}
      slotProps={backgroundProperty}>
      <FormHat title={title} onClose={handleClose} />
      <CertificateForm certificate={certificate} onClose={handleClose} />
    </Dialog>
  );
};

export default AddEditCertificateModal;
