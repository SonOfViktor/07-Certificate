import {Fab, Zoom} from '@mui/material';
import ArrowUpwardIcon from '@mui/icons-material/ArrowUpward';
import {useUpScrollButtonVisibility} from './hooks/useUpScrollButtonVisibility';

const UpScrollButton = ({upScrollElement}) => {
  const upScrollButtonVisibility = useUpScrollButtonVisibility(upScrollElement);

  return (
    <Zoom
      sx={{position: 'fixed', top: '90vh', right: '20px'}}
      in={upScrollButtonVisibility}
      unmountOnExit>
      <Fab
        color="secondary"
        onClick={() => {
          window.scrollTo({top: 0, behavior: 'smooth'});
        }}>
        <ArrowUpwardIcon htmlColor="white" />
      </Fab>
    </Zoom>
  );
};

export default UpScrollButton;
