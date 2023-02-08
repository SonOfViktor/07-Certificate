import {Button, Typography} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import {useLocation, useNavigate} from 'react-router-dom';

const buttonBackStyle = {
  position: 'absolute',
  fontSize: 16,
  bottom: 16,
  right: 16,
};

const NotFoundPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const fromPage = location.state?.from?.pathname || '/';

  return (
    <>
      <main className="main-flex">
        <Typography fontSize="60px" fontWeight="500">
          404 NotFound
        </Typography>
        <Button
          color="secondary"
          startIcon={<ArrowBackIcon />}
          sx={buttonBackStyle}
          onClick={() => navigate(fromPage)}>
          Back
        </Button>
      </main>
    </>
  );
};

export default NotFoundPage;
