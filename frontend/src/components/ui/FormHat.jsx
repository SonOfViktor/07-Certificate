import {Box, IconButton} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';

const FormHat = ({title, onClose}) => (
  <Box
    component="h2"
    sx={{
      padding: '15px 20px',
      backgroundColor: '#757575',
      color: 'white',
      fontSize: 18,
      fontWeight: 500,
    }}>
    <span>{title}</span>
    {onClose && (
      <IconButton
        onClick={onClose}
        disableRipple
        sx={{
          position: 'absolute',
          right: 8,
          top: 8,
          color: 'white',
        }}>
        <CloseIcon />
      </IconButton>
    )}
  </Box>
);

export default FormHat;
