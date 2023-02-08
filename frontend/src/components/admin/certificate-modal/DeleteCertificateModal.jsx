import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import {useDispatch} from 'react-redux';
import {deleteCertificate} from '../../../store/certificates/certificateSlice';
import FormHat from '../../ui/FormHat';
import TwoButtonGroup from '../../ui/TwoButtonGroup';
import {backgroundProperty} from './const/modalProperty';

const DeleteCertificateModal = ({open, handleClose, certificate}) => {
  const dispatch = useDispatch();

  const handleDelete = id => {
    dispatch(deleteCertificate(id));
    handleClose();
  };

  return (
    <Dialog open={open} onClose={handleClose} slotProps={backgroundProperty}>
      <FormHat title="Delete Confirmation" onClose={handleClose}></FormHat>

      <DialogContent>
        <DialogContentText>
          {`Do you really want to delete certificate ${certificate?.name}`}
        </DialogContentText>
      </DialogContent>

      <DialogActions>
        <TwoButtonGroup
          button1="No"
          button2="Yes"
          onClick1={handleClose}
          onClick2={() => handleDelete(certificate?.giftCertificateId)}
        />
      </DialogActions>
    </Dialog>
  );
};

export default DeleteCertificateModal;
