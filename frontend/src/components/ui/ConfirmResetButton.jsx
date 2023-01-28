import {Button, styled} from '@mui/material';

const StyledButton = styled(Button)(({cancel}) => ({
  color: cancel ? '#757575' : 'white',
  fontFamily: 'Segoe UI',
  fontSize: 16,
  fontWeight: 600,
  textTransform: 'none',
  flexGrow: 1,
  backgroundColor: cancel ? 'white' : '#40d47e',
  '&:hover': {
    backgroundColor: cancel && '#fbd8ef',
  },
}));

const ConfirmResetButton = ({children, onClick, ...props}) => {
  return (
    <StyledButton variant="contained" onClick={onClick} {...props}>
      {children}
    </StyledButton>
  );
};

export default ConfirmResetButton;
