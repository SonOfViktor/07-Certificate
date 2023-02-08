import {IconButton} from '@mui/material';

const HoverIconButton = props => {
  return (
    <IconButton sx={{'&:hover': {color: 'primary.main'}}} {...props}>
      {props.children}
    </IconButton>
  );
};

export default HoverIconButton;
