import {
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
} from '@mui/material';
import CancelIcon from '@mui/icons-material/Cancel';

const CloseMenuButton = ({onClose}) => {
  return (
    <ListItem disablePadding>
      <ListItemButton onClick={onClose}>
        <ListItemIcon sx={{color: 'var(--primary)'}}>
          <CancelIcon />
        </ListItemIcon>
        <ListItemText
          primary="Close"
          primaryTypographyProps={{
            fontSize: 16,
            fontWeight: 500,
          }}
        />
      </ListItemButton>
    </ListItem>
  );
};

export default CloseMenuButton;
